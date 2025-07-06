import requests
import json

def count_products(url):
    """
    Fetches product data from a URL and counts the number of products.

    Args:
        url: The URL of the products JSON endpoint.

    Returns:
        The number of products, or None if an error occurs.
    """
    try:
        # 发送 GET 请求
        response = requests.get(url)
        # 检查响应状态码，如果不是 200 OK，则抛出异常
        response.raise_for_status()

        # 解析 JSON 数据
        data = response.json()

        # 检查 'products' 键是否存在
        if 'products' in data and isinstance(data['products'], list):
            # 返回 'products' 列表的长度
            return len(data['products'])
        else:
            print("错误: JSON响应中没有找到 'products' 列表。")
            return None

    except requests.exceptions.RequestException as e:
        print(f"错误: 请求失败 - {e}")
        return None
    except json.JSONDecodeError:
        print("错误: 解析JSON失败。")
        return None

if __name__ == "__main__":
    api_url = "https://famme.no/products.json"
    product_count = count_products(api_url)

    if product_count is not None:
        print(f"接口返回的产品总数是: {product_count}")
