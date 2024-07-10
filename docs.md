## Book EndPoint

### POST /book/add
- **Description:** Add a new book
- **Parameters:** `title`, `author`, `description`, `image`

### PUT /book/edit
- **Description:** Edit an existing book
  - **Body:**
    ```json
    {
      "title": "hi1",
      "new_title": "hello1",
      "new_author": "Mg All"
    }
    ```
    - **Response:**
    ```json
      {
          "id": 6,
          "title": "Hehe6",
          "description": "hello",
          "available": true,
          "path": "/static/img/20240701214418_c3f9f5ede79df1007e343116ee823a96.png",
          "userEmail": null,
          "returnDate": null,
          "author": {
              "id": 1,
              "name": "Mg Zaw"
          }
      }
    ```

### GET /book
- **Description:** Get all books

### GET /book/{id}
- **Description:** Get a specific book by ID
- **Response:**
    ```json
      {
          "id": 6,
          "title": "Hehe6",
          "description": "hello",
          "available": true,
          "path": "/static/img/20240701214418_c3f9f5ede79df1007e343116ee823a96.png",
          "userEmail": null,
          "returnDate": null,
          "author": {
              "id": 1,
              "name": "Mg Zaw"
          }
      }
    ```

### DELETE /book/delete
- **Description:** Delete a book
- **Body:**
  ```json
  {
    "title": "Hehe3"
  }
  ```

### POST /book/borrow/{id}
- **Description:** Borrow a book by ID
- **Response:**
    ```json
      {
          "id": 6,
          "title": "Hehe6",
          "description": "hello",
          "available": false,
          "path": "/static/img/20240701214418_c3f9f5ede79df1007e343116ee823a96.png",
          "userEmail": "tester02@java.com",
          "returnDate": "2024-07-08",
          "author": {
              "id": 1,
              "name": "Mg Zaw"
          }
      }
    ```

### POST /book/return/{id}
- **Description:** Return a book by ID
  - **Response:**
      ```json
        {
            "fees": 150,
            "book": {
                "id": 5,
                "title": "Hehe5",
                "description": "hello",
                "available": true,
                "path": null,
                "userEmail": null,
                "returnDate": "2024-07-08",
                "author": {
                  "id": 1,
                  "name": "Mg Zaw"
                }
            }
    }
    ```

---

## Authentication EndPoint

### POST /auth/admin
- **Description:** Admin login
- **Body:**
  ```json
  {
    "email": "admin01@spring.com",
    "password": "admin"
  }
  ```
- **Response:**
  ```json
    {
      "key": "2b6c06a9-1ce8-4c21-b67c-9da1142a9648"
    }
  ```

### POST /auth
- **Description:** Admin login
- **Body:**
  ```json
  {
    "email": "admin01@spring.com",
    "password": "admin"
  }
  ```
- **Response:**
  ```json
    {
      "key": "2b6c06a9-1ce8-4c21-b67c-9da1142a9648"
    }
  ```

---

## Admin EndPoint

### POST /admin/add
- **Description:** Add a new admin
- **Body:**
  ```json
  {
    "email": "admin01@spring.com",
    "password": "admin01"
  }
  ```

---

## Author EndPoint

### POST /author/add
- **Description:** Add a new author
- **Body:**
  ```json
  {
    "name": "Mg Zaw"
  }
  ```

### GET /author
- **Description:** Get authors' information

---

## User EndPoint

### POST /user/add
- **Description:** Add a new user
- **Body:**
  ```json
  {
    "email": "tester02@java.com",
    "role": "bachelor",
    "fullName": "tester02",
    "password": "tester02"
  }
  ```

---

## User Profile

### GET /profile
- **Description:** Get personal details