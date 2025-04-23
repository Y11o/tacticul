import sys
import requests
import os


def send_telegram_message(token, chat_id, text):
    """Отправляет сообщение через Telegram Bot API."""
    url = f"https://api.telegram.org/bot{token}/sendMessage"
    payload = {
        'chat_id': chat_id,
        'text': text
    }
    try:
        response = requests.post(url, data=payload)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Ошибка при отправке сообщения: {e}")
        return None


def main():
    if len(sys.argv) != 3:
        print("Использование: python send_password.py <telegram_user_id> <new_password>")
        sys.exit(1)

    telegram_user_id = sys.argv[1]
    new_password = sys.argv[2]

    bot_token = os.getenv('TELEGRAM_BOT_TOKEN')
    if not bot_token:
        print("Ошибка: переменная окружения TELEGRAM_BOT_TOKEN не установлена.")
        sys.exit(1)

    message = f"Ваш новый пароль для сайта: {new_password}"

    response = send_telegram_message(bot_token, telegram_user_id, message)

    if response and response.get('ok'):
        print("Сообщение успешно отправлено.")
    else:
        print("Не удалось отправить сообщение.")
        if response:
            print("Описание ошибки:", response.get('description'))


if __name__ == "__main__":
    main()