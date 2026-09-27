# URL Shortener

## Demo / Screenshots

### Home Screen

![Home Screenome.png

---

### Analytics

![reenshots/analytics.png

---

### Top 3 URLs

![creenshots/top-urls.png

---

### Expired URL Handling

![reenshots/expired-link.png
---

## Features

- Generate short URLs from long URLs
- Custom Alias support (e.g., `/google`, `/github`)
- Prevent duplicate URL mappings
- Redirect to original URLs
- Track click counts for every short URL
- View URL Analytics
- View Top 3 Most Visited URLs
- Automatic URL expiration handling
- Copy-to-clipboard functionality
- User-friendly validation and error handling

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- HTML5
- CSS3
- JavaScript

---

## Getting Started

### Prerequisites

- Java 21+
- Maven
- MySQL

### Database Setup

Create a database:

```sql
CREATE DATABASE url_shortener;
```

Configure the following environment variables:

```text
DB_USERNAME
DB_PASSWORD
```

Example:

```text
DB_USERNAME=root
DB_PASSWORD=password
```

---

### Installation

#### Clone the repository

```bash
git clone https://github.com/jeevitha9801/url-shortener.git
```

#### Navigate to the project folder

```bash
cd url-shortener
```

#### Build the project

```bash
mvn clean install
```

#### Run the application

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

---

## How to Use

### Create a Short URL

1. Enter a valid URL
2. Optionally provide a custom alias
3. Click **Create Short URL**

Example:

```text
Original URL:
https://google.com

Custom Alias:
google

Generated URL:
http://localhost:8080/r/google
```

---

### Copy URL

Click the **Copy** button.

A success notification is displayed and the generated URL is copied to the clipboard.

---

### Open URL

Click **Open**.

The short URL redirects to the original URL in a new browser tab.

---

### Analytics

Click **Analytics** to view:

- Short Code
- Original URL
- Click Count
- Created Date
- Expiration Date

---

### Top URLs

Click **Top URLs** to view:

- Top 3 Most Visited URLs
- Click count leaderboard

---

### Expired URLs

When an expired URL is accessed:

```text
Short URL has expired
```

A modal popup is displayed instead of opening the URL.

---

## How It Works

### URL Creation Flow

```text
User
 ↓
Enter Long URL
 ↓
Generate Short Code / Custom Alias
 ↓
Store Mapping in MySQL
 ↓
Return Short URL
```

---

### URL Redirection Flow

```text
Short URL
 ↓
Lookup Short Code
 ↓
Validate Expiration
 ↓
Increment Click Count
 ↓
Redirect to Original URL
```

---

### Analytics Flow

```text
Short Code
 ↓
Fetch URL Details
 ↓
Display Click Statistics
```

---

### URL Expiration Flow

```text
Request
 ↓
Validate Expiration Date
 ↓
Expired?
 ├── Yes → Show Expired Popup
 └── No  → Continue Redirect
```

---

## API Endpoints

### Create Short URL

```http
POST /api/urls
```

Request:

```json
{
  "originalUrl": "https://google.com",
  "customAlias": "google"
}
```

---

### Redirect URL

```http
GET /r/{shortCode}
```

---

### URL Analytics

```http
GET /api/urls/{shortCode}/analytics
```

---

### Top URLs

```http
GET /api/urls/top
```

---

### URL Status

```http
GET /api/urls/{shortCode}/status
```

Used internally to validate URL expiration before opening links.

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   ├── controller
│   │   │   └── UrlController
│   │   │
│   │   ├── service
│   │   │   └── UrlService
│   │   │
│   │   ├── repository
│   │   │   └── UrlRepository
│   │   │
│   │   ├── entity
│   │   │   └── UrlMapping
│   │   │
│   │   └── dto
│   │       ├── UrlRequest
│   │       ├── UrlResponse
│   │       ├── AnalyticsResponse
│   │       └── TopUrlResponse
│   │
│   └── resources
│       ├── static
│       │   ├── index.html
│       │   ├── style.css
│       │   └── script.js
│       │
│       └── application.properties
│
└── pom.xml
```

---

## Future Enhancements

- Redis caching for frequently accessed URLs
- QR code generation
- User authentication and authorization
- URL usage history
- Cloud deployment (Azure / AWS)
- Custom expiration periods

---

## License

Licensed under the MIT License.
