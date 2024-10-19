#!/bin/bash
set -e

# Проверка, инициализирована ли база данных
if [ ! -d "/var/lib/postgresql/data/base" ]; then
    # Инициализация базы данных с нужной локалью
    initdb --locale=ru_RU.UTF-8 -D /var/lib/postgresql/data
fi

# Запуск PostgreSQL
exec "$@"