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
        # Send a GET request
        response = requests.get(url)
        # Check the response status code, raise an exception if it's not 200 OK
        response.raise_for_status()

        # Parse JSON data
        data = response.json()

        # Check if the 'products' key exists
        if 'products' in data and isinstance(data['products'], list):
            # Return the length of the 'products' list
            return len(data['products'])
        else:
            print("Error: 'products' list not found in JSON response.")
            return None

    except requests.exceptions.RequestException as e:
        print(f"Error: Request failed - {e}")
        return None
    except json.JSONDecodeError:
        print("Error: Failed to parse JSON.")
        return None

if __name__ == "__main__":
    api_url = "https://famme.no/products.json"
    product_count = count_products(api_url)

    if product_count is not None:
        print(f"The total number of products returned by the API is: {product_count}")
