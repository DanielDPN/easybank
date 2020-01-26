# Easy Bank
API Restful genérica para serviços bancários.

## Configurações
1. Java
2. Spring Boot
3. Maven
4. H2 Database

## Rodando a API
Utilizando o terminal do sistema operacional, navegue até a pasta raiz do projeto e execute o comando:

```
mvn spring-boot:run
```

Todas as solicitações para a API requerem o uso de uma chave de acesso.

Utilize o Postman ou outra ferramenta para executar as requisições.

## Obter Chave de Acesso
```http
POST http://localhost:8080/oauth/token
```

### Authorization
| Type | Username | Password |
| :--- | :--- | :--- |
| `Basic Auth` | **easy-bank** | **easy-bank** |

### Body
form-data

| Parameter | Type | Value |
| :--- | :--- | :--- |
| `grant_type` | `string` | **password** |
| `username` | `string` | **manager** |
| `password` | `string` | **manager** |

### Responses
```
{
    "access_token": "0e3f277f-4378-4bd3-b3cd-9c8929f416e4",
    "token_type": "bearer",
    "refresh_token": "3cc239cd-33d4-4383-960c-c8d7674310a2",
    "expires_in": 1799,
    "scope": "all"
}
```

## Criar Banco
```http
POST http://localhost:8080/bank
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Body
row JSON(application/json)
```
{
	"code": "033",
	"name": "Banco Santander (Brasil) S.A"
}
```

### Responses
```
{
    "id": 1,
    "code": "033",
    "name": "Banco Santander (Brasil) S.A"
}
```

## Listar Bancos
```http
GET http://localhost:8080/bank
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Responses
```
[
    {
        "id": 1,
        "code": "033",
        "name": "Banco Santander (Brasil) S.A"
    }
]
```

## Criar Agência
```http
POST http://localhost:8080/agency
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Body
row JSON(application/json)
```
{
	"bank": {
	    "id": 1,
	    "code": "033",
	    "name": "Banco Santander (Brasil) S.A"
	},
	"number": "12345",
	"digit": "X"
}
```

### Responses
```
{
    "id": 1,
    "bank": {
        "id": 1,
        "code": "033",
        "name": "Banco Santander (Brasil) S.A"
    },
    "number": "12345",
    "digit": "X"
}
```

## Listar Agências
```http
GET http://localhost:8080/agency
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Responses
```
[
    {
        "id": 1,
        "bank": {
            "id": 1,
            "code": "033",
            "name": "Banco Santander (Brasil) S.A"
        },
        "number": "12345",
        "digit": "X"
    }
]
```

## Criar Conta
```http
POST http://localhost:8080/account
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Body
row JSON(application/json)
```
{
	"agency": {
        "id": 1,
        "bank": {
            "id": 1,
            "code": "033",
            "name": "Banco Santander (Brasil) S.A"
        },
        "number": "12345",
        "digit": "X"
    },
	"client": {
		"user": {
			"name": "John",
			"email": "john",
			"password": "123456"
		},
		"name": "John Smith",
		"federalRegistration": "032.612.650-21"
	},
	"number": "123456",
	"digit": "0",
	"balance": 100
}
```

### Responses
```
{
    "id": 1,
    "agency": {
        "id": 1,
        "bank": {
            "id": 1,
            "code": "033",
            "name": "Banco Santander (Brasil) S.A"
        },
        "number": "12345",
        "digit": "X"
    },
    "client": {
        "id": 1,
        "user": {
            "id": 2,
            "name": "John",
            "email": "john",
            "password": "{bcrypt}$2a$10$HNhVNWobxqOeC1nA4HZRaugN6q0AXDZZiVTTusxd43qnuphYDMrzy",
            "roles": [
                {
                    "id": 2,
                    "name": "ROLE_CLIENT",
                    "authority": "ROLE_CLIENT"
                }
            ]
        },
        "name": "John Smith",
        "federalRegistration": "032.612.650-21"
    },
    "number": "123456",
    "digit": "0",
    "balance": 100
}
```

## Listar Contas
```http
GET http://localhost:8080/account
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Responses
```
[
    {
        "id": 1,
        "agency": {
            "id": 1,
            "bank": {
                "id": 1,
                "code": "033",
                "name": "Banco Santander (Brasil) S.A"
            },
            "number": "12345",
            "digit": "X"
        },
        "client": {
            "id": 1,
            "user": {
                "id": 2,
                "name": "John",
                "email": "john",
                "password": "{bcrypt}$2a$10$HNhVNWobxqOeC1nA4HZRaugN6q0AXDZZiVTTusxd43qnuphYDMrzy",
                "roles": [
                    {
                        "id": 2,
                        "name": "ROLE_CLIENT",
                        "authority": "ROLE_CLIENT"
                    }
                ]
            },
            "name": "John Smith",
            "federalRegistration": "032.612.650-21"
        },
        "number": "123456",
        "digit": "0",
        "balance": 100
    }
]
```

## Depositar
```http
PUT http://localhost:8080/account/deposit
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Body
row JSON(application/json)
```
{
	"account": {
		"id": 1
	},
	"amount": 500
}
```

### Responses
```
{
    "id": 1,
    "agency": {
        "id": 1,
        "bank": {
            "id": 1,
            "code": "033",
            "name": "Banco Santander (Brasil) S.A"
        },
        "number": "12345",
        "digit": "X"
    },
    "client": {
        "id": 1,
        "user": {
            "id": 2,
            "name": "John",
            "email": "john",
            "password": "{bcrypt}$2a$10$HNhVNWobxqOeC1nA4HZRaugN6q0AXDZZiVTTusxd43qnuphYDMrzy",
            "roles": [
                {
                    "id": 2,
                    "name": "ROLE_CLIENT",
                    "authority": "ROLE_CLIENT"
                }
            ]
        },
        "name": "John Smith",
        "federalRegistration": "032.612.650-21"
    },
    "number": "123456",
    "digit": "0",
    "balance": 600
}
```

## Sacar
```http
PUT http://localhost:8080/account/withdraw
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Body
row JSON(application/json)
```
{
	"account": {
		"id": 1
	},
	"amount": 500
}
```

### Responses
```
{
    "id": 1,
    "agency": {
        "id": 1,
        "bank": {
            "id": 1,
            "code": "033",
            "name": "Banco Santander (Brasil) S.A"
        },
        "number": "12345",
        "digit": "X"
    },
    "client": {
        "id": 1,
        "user": {
            "id": 2,
            "name": "John",
            "email": "john",
            "password": "{bcrypt}$2a$10$HNhVNWobxqOeC1nA4HZRaugN6q0AXDZZiVTTusxd43qnuphYDMrzy",
            "roles": [
                {
                    "id": 2,
                    "name": "ROLE_CLIENT",
                    "authority": "ROLE_CLIENT"
                }
            ]
        },
        "name": "John Smith",
        "federalRegistration": "032.612.650-21"
    },
    "number": "123456",
    "digit": "0",
    "balance": 100
}
```

## Transferir
```http
PUT http://localhost:8080/account/transfer
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Body
row JSON(application/json)
```
{
	"origin": {
		"id": 1
	},
	"destination": {
		"id": 2
	},
	"amount": 75
}
```

### Responses
```
{
    "id": 2,
    "agency": {
        "id": 1,
        "bank": {
            "id": 1,
            "code": "033",
            "name": "Banco Santander (Brasil) S.A"
        },
        "number": "12345",
        "digit": "X"
    },
    "client": {
        "id": 2,
        "user": {
            "id": 3,
            "name": "Martha Smith",
            "email": "martha",
            "password": "{bcrypt}$2a$10$DYoGNIN3cPvFzmhSf5e3XuvFr0r.LB8lK7qVOg.I9AYUhBowGU/9K",
            "roles": [
                {
                    "id": 2,
                    "name": "ROLE_CLIENT",
                    "authority": "ROLE_CLIENT"
                }
            ]
        },
        "name": "Martha Smith",
        "federalRegistration": "123.456.789-01"
    },
    "number": "654321",
    "digit": "0",
    "balance": 175
}
```

## Extrato
```http
GET http://localhost:8080/account/extract?id=1
```

### Headers
Utilizando o access_token obtido anteriormente

| Key | Value |
| :--- | :--- |
| `Authorization` | **Bearer 0e3f277f-4378-4bd3-b3cd-9c8929f416e4** |
| `Content-Type` | **application/json** |

### Responses
```
[
    {
        "id": 1,
        "type": "DEPOSIT",
        "amount": 500,
        "origin": null,
        "destination": {
            "id": 1,
            "agency": {
                "id": 1,
                "bank": {
                    "id": 1,
                    "code": "033",
                    "name": "Banco Santander (Brasil) S.A"
                },
                "number": "12345",
                "digit": "X"
            },
            "client": {
                "id": 1,
                "user": {
                    "id": 2,
                    "name": "John",
                    "email": "john",
                    "password": "{bcrypt}$2a$10$HNhVNWobxqOeC1nA4HZRaugN6q0AXDZZiVTTusxd43qnuphYDMrzy",
                    "roles": [
                        {
                            "id": 2,
                            "name": "ROLE_CLIENT",
                            "authority": "ROLE_CLIENT"
                        }
                    ]
                },
                "name": "John Smith",
                "federalRegistration": "032.612.650-21"
            },
            "number": "123456",
            "digit": "0",
            "balance": 25
        }
    },
    {
        "id": 2,
        "type": "WITHDRAW",
        "amount": 500,
        "origin": {
            "id": 1,
            "agency": {
                "id": 1,
                "bank": {
                    "id": 1,
                    "code": "033",
                    "name": "Banco Santander (Brasil) S.A"
                },
                "number": "12345",
                "digit": "X"
            },
            "client": {
                "id": 1,
                "user": {
                    "id": 2,
                    "name": "John",
                    "email": "john",
                    "password": "{bcrypt}$2a$10$HNhVNWobxqOeC1nA4HZRaugN6q0AXDZZiVTTusxd43qnuphYDMrzy",
                    "roles": [
                        {
                            "id": 2,
                            "name": "ROLE_CLIENT",
                            "authority": "ROLE_CLIENT"
                        }
                    ]
                },
                "name": "John Smith",
                "federalRegistration": "032.612.650-21"
            },
            "number": "123456",
            "digit": "0",
            "balance": 25
        },
        "destination": null
    },
    {
        "id": 3,
        "type": "TRANSFER",
        "amount": 75,
        "origin": {
            "id": 1,
            "agency": {
                "id": 1,
                "bank": {
                    "id": 1,
                    "code": "033",
                    "name": "Banco Santander (Brasil) S.A"
                },
                "number": "12345",
                "digit": "X"
            },
            "client": {
                "id": 1,
                "user": {
                    "id": 2,
                    "name": "John",
                    "email": "john",
                    "password": "{bcrypt}$2a$10$HNhVNWobxqOeC1nA4HZRaugN6q0AXDZZiVTTusxd43qnuphYDMrzy",
                    "roles": [
                        {
                            "id": 2,
                            "name": "ROLE_CLIENT",
                            "authority": "ROLE_CLIENT"
                        }
                    ]
                },
                "name": "John Smith",
                "federalRegistration": "032.612.650-21"
            },
            "number": "123456",
            "digit": "0",
            "balance": 25
        },
        "destination": {
            "id": 2,
            "agency": {
                "id": 1,
                "bank": {
                    "id": 1,
                    "code": "033",
                    "name": "Banco Santander (Brasil) S.A"
                },
                "number": "12345",
                "digit": "X"
            },
            "client": {
                "id": 2,
                "user": {
                    "id": 3,
                    "name": "Martha Smith",
                    "email": "martha",
                    "password": "{bcrypt}$2a$10$DYoGNIN3cPvFzmhSf5e3XuvFr0r.LB8lK7qVOg.I9AYUhBowGU/9K",
                    "roles": [
                        {
                            "id": 2,
                            "name": "ROLE_CLIENT",
                            "authority": "ROLE_CLIENT"
                        }
                    ]
                },
                "name": "Martha Smith",
                "federalRegistration": "123.456.789-01"
            },
            "number": "654321",
            "digit": "0",
            "balance": 175
        }
    }
]
```
