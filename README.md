# Easy Bank
API Restful genérica para serviços bancários.

Todas as solicitações para a API requerem o uso de uma chave de acesso.

## Authorization

```http
POST http://localhost:8080/oauth/token
```

| Type | Username | Password |
| :--- | :--- | :--- |
| `Basic Auth` | **easy-bank** | **easy-bank** |

## Body
| Parameter | Type | Value |
| :--- | :--- | :--- |
| `grant_type` | `string` | **password** |
| `username` | `string` | **manager** or **client** |
| `password` | `string` | **manager** or **client** |

## Responses

```javascript
{
    "access_token": "cabba1da-bb8e-4ef6-9091-f5a381af309c",
    "token_type": "bearer",
    "refresh_token": "18aa11e5-b967-4f5c-9262-da0addd04e60",
    "expires_in": 49872,
    "scope": "all"
}
```