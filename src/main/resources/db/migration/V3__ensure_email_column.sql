-- Migration: S'assurer que la colonne email existe dans la table users
-- Cette migration vérifie et corrige la structure de la table users

-- Vérifier si la colonne 'username' existe et la renommer en 'email'
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'users'
        AND column_name = 'username'
    ) THEN
        ALTER TABLE users RENAME COLUMN username TO email;
    END IF;
END $$;

-- S'assurer que la colonne email existe (au cas où ni username ni email n'existeraient)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'users'
        AND column_name = 'email'
    ) THEN
        ALTER TABLE users ADD COLUMN email VARCHAR(255) NOT NULL UNIQUE;
    END IF;
END $$;
