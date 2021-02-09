# RTB
Real Time Bidding Agent

Using Scala and Akka framework create a real-time bidding agent. Real-time bidding agent is a simple HTTP server that accepts JSON requests, does some matching between advertising campaigns and the received request and responds with a JSON response.

Requirements:
- Use Scala, Akka for bidder agent implementation
- Use Akka HTTP for HTTP server

General Notes:
- The bid request and response protocols provided above are excerpts from OpenRTB protocol, but you can use any values, for your cases. More information about OpenRTB protocol: https://www.iab.com/wp-content/uploads/2015/06/OpenRTB-API-Specification-Version-2-3.pdf
- Upon receiving a bid request, bidding agent should validate if this request is something that we want to bid on (based on your campaign list) and if so – we respond with a bid response JSON.
- If bidding agent is not going to bid on the request, it must respond with HTTP 204: No content.
