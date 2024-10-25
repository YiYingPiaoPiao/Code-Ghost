package com.alicia.projects.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/")
public class Website {

    @RequestMapping("/")
    public String Index (

            @CookieValue(
                    name         = "userid",
                    defaultValue = "none") String userId
    ) {

        if (userId.equals("none")) {

            return "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "    <title>专业论文选题管理系统</title>\n" +
                    "\n" +
                    "    <link rel=\"stylesheet\" href=\"./css/index.css\" type=\"text/css\">\n" +
                    "\n" +
                    "    <script src=\"./js/index.js\" type=\"text/javascript\"></script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    \n" +
                    "\n" +
                    "    <div id=\"headerbar\">\n" +
                    "\n" +
                    "        <table border=\"0em\">\n" +
                    "            <tr>\n" +
                    "                <td></td>\n" +
                    "                <td colspan=\"2\">专业论文选题系统</td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </div>\n" +
                    "    <div id=\"bodyarea\">\n" +
                    "\n" +
                    "        <table border=\"0em\">\n" +
                    "\n" +
                    "            <tr>\n" +
                    "                <td colspan=\"3\">\n" +
                    "\n" +
                    "                    <div id=\"noticemodule\">\n" +
                    "\n" +
                    "                        <p id=\"noticetitle\">通知</p>\n" +
                    "                    </div>\n" +
                    "                </td>\n" +
                    "                <td colspan=\"2\">\n" +
                    "\n" +
                    "                    <div id=\"loginmodule\">\n" +
                    "\n" +
                    "                        <p>登录系统</p>\n" +
                    "\n" +
                    "                        <table border=\"0em\">\n" +
                    "                            <tr>\n" +
                    "                                <td class=\"tipsarea\">用户 ID</td>\n" +
                    "                                <td colspan=\"2\">\n" +
                    "                                    <p class=\"inputarea\" contenteditable=\"true\" id=\"userid\" style=\"font-size: 1.2em; text-align: left; padding: 0.25em\"></p>\n" +
                    "                                </td>\n" +
                    "                            </tr>\n" +
                    "                            <tr>\n" +
                    "                                <td class=\"tipsarea\">用户密码</td>\n" +
                    "                                <td colspan=\"2\">\n" +
                    "                                    <p class=\"inputarea\" contenteditable=\"true\" id=\"userpwd\" style=\"font-size: 1.2em; text-align: left; padding: 0.25em\"></p>\n" +
                    "                                </td>\n" +
                    "                            </tr>\n" +
                    "                            <tr>\n" +
                    "                                <td colspan=\"3\">\n" +
                    "                                    <p id=\"loginbtn\" style=\"font-size: 1.2em;\">登录</p>\n" +
                    "                                </td>\n" +
                    "                            </tr>\n" +
                    "                        </table>\n" +
                    "                    </div>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";
        }

        return "Hello World";
    }
}
