import json

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options

from selenium.common.exceptions import NoSuchElementException


def get_keywords():

    with open('../property/keywords.txt', 'r', encoding='UTF-8') as f_keywords:

        list_keywords = [line.strip() for line in f_keywords]

    return list_keywords


def get_connect_link():

    with open('../property/bit_link.txt', 'r', encoding='UTF-8') as f:

        link = f.readline()

    return link


def search_by_keyword(browser, keyword):

    # 设置搜索选项
    browser.execute_script('document.getElementsByTagName("input")[11].setAttribute("searchname", "keyword");')
    browser.execute_script('document.getElementsByTagName("input")[11].setAttribute("value",      "关键词");')

    input_keyword = browser.find_elements(By.TAG_NAME, 'input')
    browser.execute_script('document.getElementsByTagName("input")[12].value=""')
    input_keyword[12].send_keys(keyword)

    browser.execute_script('document.getElementById("ceditor_einput").setAttribute("checkvalue", "1b2342ed7445000");')
    browser.execute_script('document.getElementById("ceditor_einput").value = "1251-工商管理";')

    browser.execute_script('document.getElementsByTagName("input")[15].setAttribute("checkvalue", "120c98db000b000");')
    browser.execute_script('document.getElementsByTagName("input")[15].value = "硕士";')

    browser.execute_script('document.getElementsByTagName("input")[17].setAttribute("checkvalue", "1426449000000dc351");')
    browser.execute_script('document.getElementsByTagName("input")[17].value = "管理与经济学院";')

    input_keyword[21].click()
    # 设置搜索选项结束


def choose_slide_list(browser, keyword):

    # 点击侧边栏
    key_value = browser.find_element(By.ID, 'keyvalue')
    slide_bar = key_value.find_elements(By.TAG_NAME, 'div')

    for i in range(len(slide_bar)):

        if keyword in slide_bar[i].text:
            to_click = slide_bar[i].find_elements(By.TAG_NAME, 'a')[0]
            break

    to_click.click()


# 获取专业，跳转新的页面
def get_major(jump_link, browser):

    current_window = browser.current_window_handle

    # 打开链接
    browser.execute_script('window.open("' + jump_link + '")')

    # 切换窗口
    temp_window = browser.window_handles[-1]
    browser.switch_to.window(temp_window)

    tmp_major = browser.find_element(By.ID, 'details').find_elements(By.TAG_NAME, 'p')[7].find_element(By.TAG_NAME, 'font').text

    browser.close()
    browser.switch_to.window(current_window)

    return tmp_major


# 获取每条数据的内容
def get_paper_details(content_temp, s_info, browser):

    s_info_p = s_info.find_elements(By.TAG_NAME, 'p')

    content_temp['title'] = s_info_p[0].find_element(By.TAG_NAME, 'a').text
    content_temp['keyword'] = s_info_p[4].find_element(By.TAG_NAME, 'span').find_element(By.TAG_NAME, 'span').text
    content_temp['abstract'] = s_info_p[7].find_element(By.TAG_NAME, 'span').text
    content_temp['level'] = s_info_p[5].find_element(By.TAG_NAME, 'span').text
    content_temp['date'] = s_info_p[6].find_element(By.TAG_NAME, 'span').text
    content_temp['mentor'] = s_info_p[3].find_element(By.TAG_NAME, 'span').find_element(By.TAG_NAME, 'span').text
    content_temp['link'] = s_info_p[0].find_element(By.TAG_NAME, 'a').get_attribute('href')

    content_temp['major'] = get_major(content_temp['link'], browser)

    return content_temp


def run_prog(userName, userPwd):

    url = get_connect_link()
    keywords = get_keywords()

    # 配置浏览器并打开
    options = webdriver.ChromeOptions()
    options.add_experimental_option("detach", True)
    # options.add_argument('--headless')

    browser = webdriver.Chrome(options=options)
    browser.get(url)

    # 登录网站
    browser.find_element(By.ID, "username").send_keys(userName)
    browser.find_element(By.ID, "password").send_keys(userPwd)
    browser.find_element(By.ID, "login_submit").click()

    keywords_details = []

    # 进行搜索
    for keyword in keywords:

        search_by_keyword(browser, keyword)
        choose_slide_list(browser, keyword)

        total_num = int(browser.find_element(By.ID, 'sortlist').text)
        total_page = min(1000, total_num // 10 + 1)

        content_format = {

            "title": "",
            "keyword": "",
            "abstract": "",
            "level": "",
            "date": "",
            "major": "",
            "mentor": "",
            "link": ""
        }

        # 内容列表
        content_list = []

        for i in range(total_page):

            s_info = browser.find_elements(By.CLASS_NAME, 's-info')

            # 每一页有 10 个数据, 除了最后一页
            num_per_page = min(10, total_num)
            for j in range(num_per_page):

                content_list.append(get_paper_details(content_format, s_info[j], browser).copy())

            # 不为最后一页的时候，点击下一页
            if i < total_page - 1:

                next_page_btn = browser.find_element(By.XPATH, '//a[contains(text(), "下一页")]')
                next_page_btn.click()

            total_num -= 10

        keyword_details = {

            "keyword": keyword,
            "details": content_list,
            "school": "北京理工大学图书馆"
        }
        keywords_details.append(keyword_details)

        browser.get(url)

    browser.quit()
    return keywords_details


if __name__ == '__main__':

    temp = run_prog('1820201063', 'SeeChen0806')

    for tmp in temp:

        print("keyword: ")
        print(tmp['keyword'])
        print(json.dumps(tmp['details'], indent=4, ensure_ascii=False))


