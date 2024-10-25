import json
import requests

url = 'http://127.0.0.1:20001/'

#######################################################

json_data = {
    'request-type': 'home',
    'send-data':{
        '测试-01': 12,
        '测试-02': 56,
        '测试-03': 23,
        '测试-04': 12,
        '测试-05': 56,
        '测试-06': 1,
        '测试-07': 96,
        '测试-08': 2,
        '测试-09': 56,
        '测试-10': 12,
        '测试-11': 78,
        '测试-12': 52,
        '测试-13': 78,
        '测试-14': 98,
        '测试-15': 55,
        '测试-16': 31,
        '测试-17': 98,
        '测试-19': 56,
        '测试-20': 89,
        '测试-21': 45,
        '测试-22': 100
    }
}
response = requests.get(f'{url}/get-graph', json=json_data)
response_data = json.loads(response.text)
print(response_data)
with open('result/home.html', 'w') as f:
    content = f'<img src = \'data:image/png;base64,{response_data["response-data"]}\'>'
    f.write(content)
    f.close()

#######################################################

json_data = {

    'request-type': 'year-num-all',
    'send-data':{
        'keyword': 'ddd',
        'year-num':{
            2009: 12,
            2010: 56,
            2011: 23,
            2012: 12,
            2013: 56,
            2014: 1,
            2015: 96,
            2016: 2,
            2017: 56,
            2018: 12,
            2019: 78,
            2020: 52,
            2021: 78,
            2022: 0,
            2023: 55,
            2024: 31
        }
    }
}
response = requests.get(f'{url}/get-graph', json=json_data)
response_data = json.loads(response.text)
with open('result/year-num-all.html', 'w') as f:

    content = f'<img src = \'data:image/png;base64,{response_data["response-data"]}\'>'
    f.write(content)
    f.close()

#######################################################

json_data = {

    'request-type': 'wordcloud-year',
    'send-data':{
        'year': 2024,
        'keyword-num':{
            '测试-01': 12,
            '测试-02': 56,
            '测试-03': 23,
            '测试-04': 12,
            '测试-05': 56,
            '测试-06': 1,
            '测试-07': 96,
            '测试-08': 2,
            '测试-09': 56,
            '测试-10': 12,
            '测试-11': 78,
            '测试-12': 52,
            '测试-13': 78,
            '测试-14': 98,
            '测试-15': 55,
            '测试-16': 31,
            '测试-17': 98,
            '测试-19': 56,
            '测试-20': 89,
            '测试-21': 45,
            '测试-22': 100
        }
    }
}
response = requests.get(f'{url}/get-graph', json=json_data)
response_data = json.loads(response.text)
with open('result/wordcloud-year.html', 'w') as f:

    content = f'<img src = \'data:image/png;base64,{response_data["response-data"]}\'>'
    f.write(content)
    f.close()

#######################################################

json_data = {

    'request-type': 'wordcloud-keyword',
    'send-data':{
        'keyword': '人工智能',
        'year-num':{
            2009: 12,
            2010: 56,
            2011: 23,
            2012: 12,
            2013: 56,
            2014: 1,
            2015: 96,
            2016: 2,
            2017: 56,
            2018: 12,
            2019: 78,
            2020: 0,
            2021: 78,
            2022: 98,
            2023: 55,
            2024: 31
        }
    }
}
response = requests.get(f'{url}/get-graph', json=json_data)
response_data = json.loads(response.text)
with open('result/wordcloud-keyword.html', 'w') as f:

    content = f'<img src = \'data:image/png;base64,{response_data["response-data"]}\'>'
    f.write(content)
    f.close()

#######################################################

json_data = {

    'request-type': 'keyword-rela-1',
    'send-data':{
        'keyword-list': [['新能源汽车','数字营销','营销策略'],
        ['医药零售','新零售','数字营销','STP 理论','4R 营销理论'],
        ['数字营销','内容营销','获客','私域流量'],
        ['葡萄酒公司','数字营销','营销策略','数字化','策略制定'],
        ['进口化妆品','STP','4Ps理论','数字营销'],
        ['企业转型','数字化转型','数字化成熟度评估'],
        ['建筑企业','战略管理','数字化转型'],
        ['A 基金公司 APP 业务','数字化转型','运营管理','运营策略'],
        ['物流管理','航空物流','数字化转型','数字化策略','变革策略'],
        ['商业银行','零售业务','数字化转型'],
        ['业务流程','数字化信息系统','数字化转型','需求管理','优化方案'],
        ['证券公司','数字化转型','金融科技'],
        ['数字化转型','智能税务','效果评估','优化建议'],
        ['地产企业','资产管理','数字化转型','数字化策略'],
        ['人工智能','数字化转型','大客户管理','IDIC模型','客户生命周期'],
        ['人力资源管理','数字化转型','变革','竞争力'],
        ['数字化转型','信托集合业务','客户关系管理'],
        ['地产行业','运营管理','数字化转型','数字化管理'],
        ['跨国企业','技术转移','风险管理','数字化转型'],
        ['集体企业','数字化转型','数字经济'],
        ['数字化转型','人力资源管理','大数据','云生态'],
        ['数字化转型','国际工程企业','战略匹配','综合业务数字化平台'],
        ['数字化转型','数字化战略','数字化成熟度模型','集团公司总部'],
        ['数字营销',  '网上购物',  '决策因素'],
        ['数字营销策略',  '网站流量分析',  '搜素引擎优化',  '微博内容优化',  '舆情监听'],
        ['Baci',  '数字营销',  '营销策略',  '互联网渠道'],
        ['文本挖掘',  '消费者洞察',  '大数据应用',  '汽车',  '营销策略'],
        ['搜索引擎营销',  '数字营销',  '网络营销',  '网络推广',  '教育培训行业',  '中小型企业']]
    }
}
response = requests.get(f'{url}/get-graph', json=json_data)
response_data = json.loads(response.text)
with open('result/keyword-rela-1.html', 'w') as f:

    content = f'<img src = \'data:image/png;base64,{response_data["response-data"]}\'>'
    f.write(content)
    f.close()

#######################################################