package dev.article.article.Controller;

import dev.article.article.Repository.ArticleEntityRepository;
import dev.article.article.Service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;


@Controller
public class Website {

    private final ArticleService articleService;
    private final ArticleEntityRepository articleEntityRepository;

    public Website(ArticleEntityRepository articleEntityRepository, ArticleService articleService){this.articleService = articleService; this.articleEntityRepository = articleEntityRepository;}

    @GetMapping("/homePage")
    public String homepage(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        return  "redirect:/adminPage";
                    } else if (userAccessLvl != 0) {

                        List<Thesis> theses = new ArrayList<>();

                        String filePath = "";
                        try {
                            filePath = articleService.readLongBlobDataForLastArticleByUploadUserId("24010001");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

//        try {
//            System.out.println(articleService.readLongBlobDataForLastArticleByUploadUserId(userId));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
                        if (filePath != null) {
                            if (!filePath.equals("")) {
                                try (BufferedReader br = new BufferedReader(new StringReader(filePath))) {

                                    String line;
                                    Thesis thesis = null;

                                    while ((line = br.readLine()) != null) {

                                        if (line.startsWith("题名:")) {

                                            if (thesis != null) {
                                                theses.add(thesis);
                                            }

                                            thesis = new Thesis();
                                            thesis.setTitle(line.substring(3));

                                        } else if (line.startsWith("英文题名:")) {

                                            thesis.setEnglishTitle(line.substring(5));
                                        } else if (line.startsWith("作者:")) {

                                            thesis.setAuthor(line.substring(3));
                                        } else if (line.startsWith("学号:")) {

                                            thesis.setStudentID(line.substring(3));
                                        } else if (line.startsWith("导师:")) {

                                            thesis.setSupervisor(line.substring(3));
                                        } else if (line.startsWith("关键词:")) {

                                            String[] keywords = line.substring(4).split(",");
                                            int centerIndex = keywords.length / 2;

                                            HashMap<String, String> map = new HashMap<>();
                                            map.put((keywords[centerIndex].replaceAll("[^a-zA-Z ]", "")).trim(), keywords[0].trim());
                                            for (int i = 1; i < centerIndex; i++) {

                                                map.put(keywords[centerIndex + i].trim(), keywords[i].trim());
                                            }
                                            map.put(keywords[centerIndex + centerIndex].trim(), keywords[centerIndex].replaceAll("[a-zA-Z]", "").trim());
                                            thesis.setKeywords(map);
                                        } else if (line.startsWith("学位级别:")) {

                                            thesis.setDegreeLevel(line.substring(5));
                                        } else if (line.startsWith("答辩日期:")) {

                                            thesis.setDefenseDate(line.substring(5, 9));
                                        } else if (line.startsWith("中文摘要:")) {

                                            StringBuilder abstractText = new StringBuilder();
                                            while ((line = br.readLine()) != null && !line.startsWith("外文摘要:")) {

                                                abstractText.append(line).append("\n");
                                            }
                                            thesis.setChineseAbstract(abstractText.toString().trim());
                                        }

                                        if (line.startsWith("外文摘要:")) {

                                            StringBuilder abstractText = new StringBuilder();
                                            while ((line = br.readLine()) != null && line.length() != 0) {

                                                abstractText.append(line).append("\n");
                                            }
                                            thesis.setEnglishAbstract(abstractText.toString().trim());
                                        }
                                    }

                                    if (thesis != null) {
                                        theses.add(thesis);
                                    }

//            System.out.println(theses.size());
//            System.out.println(theses.get(0).getKeywords());
//            System.out.println(theses.get(0).getKeywords().keySet());
//            System.out.println(theses.get(0).getKeywords().values());
//            System.out.println(title);
                                    System.out.println("test test test");

                                    int articleSize = theses.size();

//            String csvFile = "year,keyword,value\n";
                                    String csvFile = "";

                                    for (int as = 0; as < articleSize; as++) {

                                        HashMap<String, String> keywordsValues = theses.get(as).getKeywords();

                                        for (String value : keywordsValues.values()) {
                                            csvFile += theses.get(as).getDefenseDate() + "," + value + ",1\n";
                                        }
                                    }

                                    String[] lines = csvFile.split("\n");
                                    Map<String, Integer> countMap = new HashMap<>();
                                    for (String liness : lines) {
                                        String[] parts = liness.split(",");
                                        String key = parts[0] + "," + parts[1]; // Combine year and keyword
                                        int count = parseInt(parts[2]); // Get the count from the line

                                        // Update the count for the key in the HashMap
                                        countMap.put(key, countMap.getOrDefault(key, 0) + count);
                                    }

                                    // StringBuilder to build the result
                                    StringBuilder result = new StringBuilder();

                                    // Iterate over the entries in the countMap
                                    for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                                        String[] parts = entry.getKey().split(",");
                                        String year = parts[0];
                                        String keyword = parts[1];
                                        int totalCount = entry.getValue(); // Get the total count

                                        // Append the year, keyword, and total count to the result
                                        result.append(year).append(",").append(keyword).append(",").append(totalCount).append("\n");
                                    }

                                    csvFile = "year,keyword,value\n" + result.toString();
//                    System.out.println(csvFile);
                                    model.addAttribute("csvFile", csvFile);


                                } catch (IOException e) {

                                    e.printStackTrace();
                                }
                            }
                        }


                        return "homePage";
                    }
                }
            }
        }


        return "redirect:/";
    }

    @GetMapping("/insertArticle")
    public String insertArticle(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = Integer.parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        return  "redirect:/adminPage";
                    } else if (userAccessLvl != 0) {
                        return "insertArticle";
                    }
                }
            }
        }
        return "redirect:/";
    }












}
