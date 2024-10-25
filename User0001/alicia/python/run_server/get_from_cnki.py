import json
import time

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait

from selenium.common.exceptions import NoSuchElementException


def get_keywords():

    with open('../property/keywords.txt', 'r', encoding='UTF-8') as f_keywords:

        list_keywords = [line.strip() for line in f_keywords]

    return list_keywords


def get_connect_link():

    with open('../property/cnki_link.txt', 'r', encoding='UTF-8') as f:

        link = f.readline()

    return link


def get_need_major():

    with open('../property/cnki_key_major.txt', 'r', encoding='UTF-8') as f:

        list_major = [line.strip() for line in f]

    return list_major


def get_need_school():
    with open('../property/cnki_key_school.txt', 'r', encoding='UTF-8') as f:
        list_school = [line.strip() for line in f]

    return list_school


def search_and_load_all_element(browser, keyword):
    # 进行第一次的搜索，使得其余的元素都加载出来

    browser.execute_script("document.getElementsByClassName('sort-list')[0].style.display = 'block'")
    browser.find_element(By.XPATH, "//*[@data-val='KY']").find_element(By.TAG_NAME, 'a').click()

    browser.execute_script("document.getElementsByClassName('sort-list')[0].style.display = 'block'")
    browser.find_element(By.XPATH, "//*[@data-val='KY']").find_element(By.TAG_NAME, 'a').click()

    browser.find_element(By.ID, 'txt_search').send_keys(keyword)

    browser.find_element(By.CLASS_NAME, 'search-btn').click()


def choose_level(browser):


    temp_script = "document.getElementsByTagName('ul')[4].getElementsByTagName('ul')[0].style.display='block'"
    browser.execute_script(temp_script)

    browser.find_element(By.XPATH, "//*[@classid='JQIRZIYA']").click()


def choose_major(browser, key_major):

    browser.find_element(By.XPATH, "//*[@groupid='SBC']").click()
    time.sleep(1)
    browser.find_element(By.XPATH, "//*[@groupid='SBC']").find_element(By.CLASS_NAME, 'btn').click()

    # 选择学科专业
    SBC_li = browser.find_element(By.XPATH, "//*[@groupid='SBC']").find_elements(By.TAG_NAME, 'li')

    for _sbc_li in SBC_li:

        if key_major == _sbc_li.find_element(By.TAG_NAME, 'a').text:

            _sbc_li.find_element(By.TAG_NAME, 'a').click()
            break


def choose_school(browser, key_school):

    browser.find_element(By.XPATH, "//*[@groupid='CBWDM']").click()
    time.sleep(1)
    browser.find_element(By.XPATH, "//*[@groupid='CBWDM']").find_element(By.CLASS_NAME, 'btn').click()

    CBWDM_li = browser.find_element(By.XPATH, "//*[@groupid='CBWDM']").find_elements(By.TAG_NAME, 'li')

    for _cbwdm_li in CBWDM_li:

        if key_school in _cbwdm_li.text:
            _cbwdm_li.click()


def get_paper_details(browser, rl, temp_format):

    temp_link = rl.find_element(By.CLASS_NAME, 'name').find_element(By.TAG_NAME, 'a').get_attribute('href')
    temp_date = rl.find_element(By.CLASS_NAME, 'date').text

    current_window = browser.current_window_handle

    browser.execute_script('window.open("' + temp_link + '")')

    temp_window = browser.window_handles[-1]
    browser.switch_to.window(temp_window)

    content_class_keywords = browser.find_elements(By.CLASS_NAME, 'keywords')

    temp_format['title'] = browser.find_element(By.TAG_NAME, 'h1').text
    temp_format['keyword'] = content_class_keywords[0].text.replace(';', ', ')
    temp_format['abstract'] = browser.find_element(By.ID, 'abstract_text').get_attribute('value')
    temp_format['level'] = '硕士'
    temp_format['date'] = temp_date
    temp_format['major'] = content_class_keywords[2].text
    temp_format['mentor'] = content_class_keywords[1].text
    temp_format['link'] = temp_link

    browser.close()
    browser.switch_to.window(current_window)

    return temp_format


def run_prog():

    # 获取链接和关键字
    url = get_connect_link()
    keywords = get_keywords()

    # 获取关键字对应的专业和学校
    keywords_major = get_need_major()
    keywords_school = get_need_school()

    keywords_details = []
    # 开始进行搜索
    for keyword, key_major, key_school in zip(keywords, keywords_major, keywords_school):

        # 配置浏览器并打开
        options = webdriver.ChromeOptions()
        options.add_experimental_option("detach", True)
        # options.add_argument('--headless')

        browser = webdriver.Chrome(options=options)
        browser.get(url)

        search_and_load_all_element(browser, keyword)
        time.sleep(10)
        choose_level(browser)
        time.sleep(1)
        choose_major(browser, key_major)
        time.sleep(1)
        choose_school(browser, key_school)
        time.sleep(1)

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

        # 获取结果数量和页面的数量
        count_page_div = browser.find_element(By.ID, 'countPageDiv')

        try:
            total_page = int(count_page_div.find_element(By.CLASS_NAME, 'countPageMark').get_attribute('data-pagenum'))

        except NoSuchElementException:

            total_page = 1

        content_list = []

        # 获取详细内容
        for i in range(total_page):

            time.sleep(1)

            result_list = browser.find_element(By.ID, 'gridTable').find_element(By.TAG_NAME, 'tbody').find_elements(By.TAG_NAME, 'tr')

            for rl in result_list:

                content_list.append(get_paper_details(browser, rl, content_format).copy())

            if i < total_page - 1:

                next_btn = browser.find_element(By.ID, 'PageNext')
                next_btn.click()

        keyword_details = {

            "keyword": keyword,
            "details": content_list,
            "school": key_school
        }
        keywords_details.append(keyword_details)

        browser.quit()

    return keywords_details


if __name__ == '__main__':

    temp = run_prog()
    for i in temp:

        print(temp)

