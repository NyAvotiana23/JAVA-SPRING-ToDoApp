-- Migration: Renommer la colonne username en email dans la table users
ALTER TABLE users RENAME COLUMN username TO email;

-- S'assurer que la contrainte d'unicité est bien présente sur email
-- (la contrainte unique existante sur username sera automatiquement transférée sur email)
