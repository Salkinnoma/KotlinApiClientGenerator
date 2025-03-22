A Kotlin library that automatically generates Retrofit interfaces from a given API endpoint. Simply provide a base API URL, and this tool will fire a request, analyze the response, and create the necessary Retrofit interfaces and data models to simplify your API integration. Perfect for quickly setting up network clients without the need for manual interface creation!

Features:
🚀 Automatic Retrofit Interface Generation: Generate Retrofit interfaces with appropriate annotations based on the API response.
📦 Data Model Generation: Automatically create Kotlin data classes from the JSON response.
⚡ Coroutine Support: Uses suspend functions for seamless integration with Kotlin coroutines.
🔄 Supports Custom API Endpoints: Generate interfaces for any given API endpoint, including pagination and query parameters.
🔧 Customizable: Easily extend and modify the generated interfaces to suit your specific needs.