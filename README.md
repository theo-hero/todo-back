## Endpoints
  
- `POST /signup/confirmation_code` sends code to email  
- `POST /signup` requires code from email to sign up  
- `POST /signin`  
- `GET /me`  
- `GET /link_telegram` returns a temporary code which points to a particular user  
  
- `POST /bot/link` checks code and saves telegram_id (request must come from telegram)  
  
- `GET /mytasks`  
- `POST /mytasks`  
- `PUT /mytasks?id=...`  
- `DELETE /mytasks?id=...`  
