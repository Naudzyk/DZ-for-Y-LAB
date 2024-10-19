DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'y_lab_db') THEN
        CREATE DATABASE y_lab_db;
    END IF;
END $$;