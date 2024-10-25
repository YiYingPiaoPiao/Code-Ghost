package dev.article.article.Controller;

import com.mongodb.client.model.InsertOneModel;
import dev.article.article.Entity.ArticleEntity;
import dev.article.article.Repository.ArticleEntityRepository;
import dev.article.article.Service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

class Thesis {
    private String title       ;
    private String englishTitle;
    private String author      ;
    private String studentID   ;
    private String supervisor  ;
    private String degreeLevel ;
    private String defenseDate ;
    private String chineseAbstract;
    private String englishAbstract;

    private HashMap<String, String> keywords;

    public Thesis() { this.keywords = new HashMap<>(); }

    public String getTitle       () { return title       ; } public void setTitle       (String title       ) { this.title        = title       ; }
    public String getEnglishTitle() { return englishTitle; } public void setEnglishTitle(String englishTitle) { this.englishTitle = englishTitle; }
    public String getAuthor      () { return author      ; } public void setAuthor      (String author      ) { this.author       = author      ; }
    public String getStudentID   () { return studentID   ; } public void setStudentID   (String studentID   ) { this.studentID    = studentID   ; }
    public String getSupervisor  () { return supervisor  ; } public void setSupervisor  (String supervisor  ) { this.supervisor   = supervisor  ; }
    public String getDegreeLevel () { return degreeLevel ; } public void setDegreeLevel (String degreeLevel ) { this.degreeLevel  = degreeLevel ; }
    public String getDefenseDate () { return defenseDate ; } public void setDefenseDate (String defenseDate ) { this.defenseDate  = defenseDate ; }

    public HashMap<String, String> getKeywords() { return keywords; } public void setKeywords(HashMap<String, String> keywords) { this.keywords = keywords; }

    public String getChineseAbstract() { return chineseAbstract; } public void setChineseAbstract(String chineseAbstract) { this.chineseAbstract = chineseAbstract; }
    public String getEnglishAbstract() { return englishAbstract; } public void setEnglishAbstract(String englishAbstract) { this.englishAbstract = englishAbstract; }

}

@Controller
public class searchAriticle {

    private final ArticleService articleService;
    private final ArticleEntityRepository articleEntityRepository;

    public searchAriticle(ArticleService articleService, ArticleEntityRepository articleEntityRepository){this.articleService = articleService; this.articleEntityRepository = articleEntityRepository;}

    @GetMapping("/searchArticle")
    public String searchArticle(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userAccessLvl = 0;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userAccessLvl")) {

                    userAccessLvl = parseInt(cookie.getValue());

                    if (userAccessLvl == 3){
                        return  "redirect:/adminPage";
                    } else if (userAccessLvl != 0) {
                        String userId = null;
                        for (Cookie c : cookies) {
                            if (c.getName().equals("userId")) {
                                userId = c.getValue();
                                String lastArticleName = articleService.getLastArticleNameByUserId(userId);
                                model.addAttribute("lastArticleName",lastArticleName);
                                return "searchArticle";
                            }
                        }
                    }
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/searchArticle")
    public String chartArticle(HttpServletRequest request,@RequestParam("title") String title, @RequestParam("startDate") int startDate, @RequestParam("endDate") int endDate,Model model){

        Cookie[] cookies = request.getCookies();
        String userId = null;
        if (cookies != null){

            for (Cookie c : cookies) {
                if (c.getName().equals("userId")) {
                    userId = c.getValue();
                    String lastArticleName = articleService.getLastArticleNameByUserId(userId);
                    model.addAttribute("lastArticleName",lastArticleName);
                }
            }
        }

        List<Thesis> theses = new ArrayList<>();

        String filePath = "";
        try {
            filePath = articleService.readLongBlobDataForLastArticleByUploadUserId(userId);
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
                    String searchTittle = "";
                    if (title.isEmpty()) {
                        searchTittle = "";
                    } else {
                        searchTittle = title;
                    }
                    ;
//            String csvFile = "year,keyword,value\n";
                    String csvFile = "";

                    for (int as = 0; as < articleSize; as++) {

                        if (!title.isEmpty()) {
                            int distance = LevenshteinDistance.getDefaultInstance().apply(searchTittle, theses.get(as).getTitle());
                            int threshold = 15;
                            boolean isSimilar = distance <= threshold;

                            if (!isSimilar) {
                                distance = LevenshteinDistance.getDefaultInstance().apply(searchTittle, theses.get(as).getEnglishTitle());
                                isSimilar = distance <= threshold;
                                if (isSimilar) {
                                    for (int i = as; i < articleSize; i++) {

                                        HashMap<String, String> keywordsValues = theses.get(as).getKeywords();

                                        for (String value : keywordsValues.values()) {
                                            // Print each value
                                            if (parseInt(theses.get(as).getDefenseDate()) > endDate || parseInt(theses.get(as).getDefenseDate()) < startDate) {
                                                continue;
                                            }
                                            csvFile += theses.get(as).getDefenseDate() + "," + value + ",1\n";
                                        }

                                    }

                                } else {
                                    System.out.println("No Result");
                                }
                            } else {
                                HashMap<String, String> keywordsValues = theses.get(as).getKeywords();

                                for (String value : keywordsValues.values()) {
                                    // Print each value
                                    if (parseInt(theses.get(as).getDefenseDate()) > endDate || parseInt(theses.get(as).getDefenseDate()) < startDate) {
                                        continue;
                                    }
                                    csvFile += theses.get(as).getDefenseDate() + "," + value + ",1\n";
                                }
                            }
                        } else {
                            HashMap<String, String> keywordsValues = theses.get(as).getKeywords();

                            for (String value : keywordsValues.values()) {
                                // Print each value
                                if (parseInt(theses.get(as).getDefenseDate()) > endDate || parseInt(theses.get(as).getDefenseDate()) < startDate) {
                                    continue;
                                }
                                csvFile += theses.get(as).getDefenseDate() + "," + value + ",1\n";
                            }
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

        return "searchArticle";
    }
}
