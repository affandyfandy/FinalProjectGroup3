# Hotel Reservation Management System

The Hotel Reservation Management System is a comprehensive system to process end-to-end hotel booking orders for customers and provides insightful information for the hotel management.

## List of Endpoint

### Hotel
| Method | Endpoint                        | Description                              |
|--------|---------------------------------|------------------------------------------|
| `GET`  | `/api/v1/hotel`               | Retrieve all hotels                   |
| `GET`  | `/api/v1/hotel/{id}`          | Get hotel details                  |
| `PUT`  | `/api/v1/hotel/{id}`          | Update room details (admin only)               |
| `POST` | `/api/v1/hotel/{id}/activate` | Set hotel status to "Active" (admin only)    |
| `POST` | `/api/v1/hotel/{id}/deactivate` | Set hotel status to "Inactive" (admin only) |
| `POST` | `/api/v1/hotel`               | Create new hotel                     |

### Room

| Method | Endpoint                        | Description                              |
|--------|---------------------------------|------------------------------------------|
| `GET`  | `/api/v1/room`               | Retrieve all rooms                   |
| `GET`  | `/api/v1/room/{id}`          | Get room details                  |
| `PUT`  | `/api/v1/room/{id}`          | Update room details (admin only)               |
| `POST` | `/api/v1/room/{id}/activate` | Set room status to "Active" (admin only)    |
| `POST` | `/api/v1/room/{id}/deactivate` | Set room status to "Inactive" (admin only) |
| `POST` | `/api/v1/room`               | Create new room                     |

### Authentication

| Method | Endpoint                        | Description                              |
|--------|---------------------------------|------------------------------------------|
| `GET` | `/api/v1/auth/login` | User login and token generation |
| `POST` | `/api/v1/auth/register` | User registration |
| `POST` | `/api/v1/auth/validate` | Token validation |

### User

| Method | Endpoint                        | Description                              |
|--------|---------------------------------|------------------------------------------|
| `GET` | `/api/v1/user/{id}` | Get user details by user Id    |
| `POST` | `/api/v1/user/{id}/deactivate` | Set user status to "Inactive" |
| `POST` | `/api/v1/user/{id}/activate` | Set user status to "Activate" |
| `GET` | `/api/v1/user`               | Retrieve all users                     |

### Reservation

| Method | Endpoint                        | Description                              |
|--------|---------------------------------|------------------------------------------|
| `GET` | `/api/v1/reservation/{id}` | Get reservation details by Id    |
| `POST` | `/api/v1/reservation` | Create new reservation |
| `PUT` | `/api/v1/reservation/{id}` | Update reservation (admin only) |
| `PATCH` | `/api/v1/reservation/{id}`       | Cancel reservation                      |

---

*Delivered with ❤ by Group 3 FINDO*