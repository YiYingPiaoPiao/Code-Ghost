# 启动页面

from flask import Flask, jsonify, request
from wordcloud import WordCloud
import networkx as nx

import io
import base64
import matplotlib.pyplot as plt

import get_from_bit as gfb
import get_from_cnki as gfc


app = Flask(__name__)

# 获取图片
@app.route('/get-graph', methods=['GET'])
def get_graph():

    all_data = request.get_json()
    print(all_data)

    request_type = all_data['request-type']
    send_data = all_data['send-data']

    response_data = {}
    
    if request_type == 'home':
        data = dict(sorted(send_data.items(), key=lambda item: item[1], reverse=True))
        keywords = list(data.keys())
        counts = list(data.values())
        fig, ax = plt.subplots()
        plt.rcParams['font.sans-serif'] = ['SimHei']
        plt.rcParams['axes.unicode_minus'] = False
        bars = ax.bar(keywords, counts, color='green')
        ax.set_xlabel('')
        ax.set_ylabel('数量')
        ax.set_title('')
        ax.grid(axis='y', linestyle='--', alpha=0.25)
        plt.xticks(rotation=75)
        plt.tight_layout()
        buffer = io.BytesIO()
        plt.savefig(buffer, format='png')
        buffer.seek(0)
        image_base64 = base64.b64encode(buffer.read()).decode('utf-8')
        plt.close()
        response_data['response-data'] = image_base64

    elif request_type == 'year-num-all':
        data = send_data['year-num']
        keyword = send_data['keyword']
        keywords = list(data.keys())
        counts = list(data.values())
        fig, ax = plt.subplots()
        plt.rcParams['font.sans-serif'] = ['SimHei']
        plt.rcParams['axes.unicode_minus'] = False
        bars = ax.bar(keywords, counts, color='green')
        ax.set_xlabel('')
        ax.set_ylabel('数量')
        ax.set_title(f'关于 "{keyword}" 关键词')
        ax.grid(axis='y', linestyle='--', alpha=0.25)
        plt.xticks(rotation=75)
        plt.tight_layout()
        buffer = io.BytesIO()
        plt.savefig(buffer, format='png')
        buffer.seek(0)
        image_base64 = base64.b64encode(buffer.read()).decode('utf-8')
        plt.close()
        response_data['response-data'] = image_base64

    elif request_type == 'wordcloud-year':
        data = send_data['keyword-num']
        year = send_data['year']
        font_path = './word.TTF'
        wordcloud = WordCloud(width=800, height=400, font_path=font_path, background_color='white')
        wordcloud.generate_from_frequencies(data)
        plt.rcParams['font.sans-serif'] = ['SimHei']
        plt.rcParams['axes.unicode_minus'] = False
        plt.figure(figsize=(10, 5))
        plt.imshow(wordcloud, interpolation='bilinear')
        plt.axis('off')
        plt.title(f'{year} 年词云图')
        buffer = io.BytesIO()
        plt.savefig(buffer, format='png')
        buffer.seek(0)
        image_base64 = base64.b64encode(buffer.read()).decode('utf-8')
        plt.close()
        response_data['response-data'] = image_base64

    elif request_type == 'wordcloud-keyword':
        data = send_data['year-num']
        keyword = send_data['keyword']
        font_path = './word.TTF'
        wordcloud = WordCloud(width=800, height=400, font_path=font_path, background_color='white')
        wordcloud.generate_from_frequencies(data)
        plt.rcParams['font.sans-serif'] = ['SimHei']
        plt.rcParams['axes.unicode_minus'] = False
        plt.figure(figsize=(10, 5))
        plt.imshow(wordcloud, interpolation='bilinear')
        plt.axis('off')
        plt.title(f'不同年份选择 “{keyword}” 词云图')
        buffer = io.BytesIO()
        plt.savefig(buffer, format='png')
        buffer.seek(0)
        image_base64 = base64.b64encode(buffer.read()).decode('utf-8')
        plt.close()
        response_data['response-data'] = image_base64
    
    elif request_type == 'keyword-rela-1':
        data = send_data['keyword-list']
        G = nx.Graph()
        plt.rcParams['font.sans-serif'] = ['SimHei']
        plt.rcParams['axes.unicode_minus'] = False
        for item in data:
            G.add_nodes_from(item)
            if len(item) > 1:
                edges = [(item[i], item[j]) for i in range(len(item)) for j in range(i + 1, len(item))]
                G.add_edges_from(edges)
        node_degrees = dict(G.degree())
        min_degree = min(node_degrees.values())
        max_degree = max(node_degrees.values())
        node_sizes = [(node_degrees[node] - min_degree + 1) * 300 for node in G.nodes()]
        node_colors = [node_degrees[node] for node in G.nodes()]
        color_map = plt.cm.get_cmap('cool')
        plt.figure(figsize=(20, 20))
        pos = nx.spring_layout(G, k=2, iterations=1)
        nx.draw(G, pos, with_labels=True, node_color=node_colors, cmap=color_map, node_size=node_sizes, font_size=12, font_color='black', edge_color='gray', linewidths=1, alpha=0.8)
        plt.title('关联图')
        buffer = io.BytesIO()
        plt.savefig(buffer, format='png')
        buffer.seek(0)
        image_base64 = base64.b64encode(buffer.read()).decode('utf-8')
        plt.close()
        response_data['response-data'] = image_base64

    response = jsonify(response_data)
    response.status_code = 200
    return response


# 获取论文数据
@app.route('/get-from-bit')
def get_from_bit():

    return jsonify(gfb.run_prog('1820201063', 'SeeChen0806'))

@app.route('/get-from-cnki')
def get_from_cnki():

    return jsonify(gfc.run_prog())


@app.route('/get-details')
def get_details():

    details = gfb.run_prog('1820201063', "SeeChen0806")
    
    cnki_s = gfc.run_prog()
    for cnki in cnki_s:
        details.append(cnki)

    print(details)

    return jsonify(details)


@app.route('/')
def index():

    return 'Hello World'


if __name__ == '__main__':

    app.run(host='0.0.0.0', port='20001')

