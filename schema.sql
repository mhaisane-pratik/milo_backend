-- ====================================================================
-- MILO PLATFORM - CLEAN POSTGRESQL / NEON DATABASE SCHEMA SCRIPT
-- Drops existing tables if any, then creates complete schema & seed data
-- Ready to run in Neon SQL Console / PostgreSQL Editor
-- ====================================================================

-- 1. DROP EXISTING TABLES IF THEY EXIST (WIPE OLD SCHEMA)
DROP TABLE IF EXISTS app_settings CASCADE;
DROP TABLE IF EXISTS announcements CASCADE;
DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS connections CASCADE;
DROP TABLE IF EXISTS group_messages CASCADE;
DROP TABLE IF EXISTS group_members CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS event_participants CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS activities CASCADE;
DROP TABLE IF EXISTS user_interests CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 2. ENABLE UUID EXTENSION
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 3. USERS TABLE
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('ADMIN', 'USER')),
    photo_url TEXT,
    college VARCHAR(150),
    bio TEXT,
    location VARCHAR(100) DEFAULT 'Wakad, Pune',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'BLOCKED', 'SUSPENDED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- USER INTERESTS TABLE
CREATE TABLE user_interests (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    interest VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, interest)
);

-- 4. ACTIVITIES CATALOG TABLE
CREATE TABLE activities (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL, -- Coffee, Gaming, Sports, Travel, Study, Trekking, Movies, Art
    description TEXT,
    icon VARCHAR(10),
    image_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. EVENTS & PLANS TABLE
CREATE TABLE events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(150) NOT NULL,
    activity_id UUID REFERENCES activities(id) ON DELETE SET NULL,
    created_by UUID REFERENCES users(id) ON DELETE CASCADE,
    description TEXT,
    location TEXT NOT NULL,
    area VARCHAR(100) NOT NULL,
    event_date VARCHAR(50) NOT NULL,
    event_time VARCHAR(50) NOT NULL,
    total_spots INT NOT NULL DEFAULT 10,
    joined_spots INT NOT NULL DEFAULT 1,
    price DECIMAL(10, 2) DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'APPROVED' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),
    is_featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- EVENT PARTICIPANTS TABLE
CREATE TABLE event_participants (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_id UUID REFERENCES events(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'JOINED' CHECK (status IN ('JOINED', 'LEFT')),
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(event_id, user_id)
);

-- 6. COMMUNITY GROUPS TABLE
CREATE TABLE groups (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    activity_id UUID REFERENCES activities(id) ON DELETE SET NULL,
    image_url TEXT,
    created_by UUID REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- GROUP MEMBERS TABLE
CREATE TABLE group_members (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_id UUID REFERENCES groups(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(20) DEFAULT 'MEMBER' CHECK (role IN ('MEMBER', 'ADMIN')),
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(group_id, user_id)
);

-- GROUP MESSAGES TABLE
CREATE TABLE group_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_id UUID REFERENCES groups(id) ON DELETE CASCADE,
    sender_id UUID REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. CONNECTIONS TABLE
CREATE TABLE connections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    requester_id UUID REFERENCES users(id) ON DELETE CASCADE,
    receiver_id UUID REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'CONNECTED' CHECK (status IN ('PENDING', 'CONNECTED', 'REMOVED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(requester_id, receiver_id)
);

-- 8. REPORTS TABLE
CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    reported_by UUID REFERENCES users(id) ON DELETE CASCADE,
    target_type VARCHAR(20) NOT NULL CHECK (target_type IN ('USER', 'EVENT', 'GROUP', 'MESSAGE')),
    target_id UUID NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'RESOLVED', 'DISMISSED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 9. ANNOUNCEMENTS TABLE
CREATE TABLE announcements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    author_id UUID REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 10. APP SETTINGS TABLE
CREATE TABLE app_settings (
    key VARCHAR(100) PRIMARY KEY,
    value TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ====================================================================
-- PERFORMANCE INDEXES
-- ====================================================================
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_events_area ON events(area);
CREATE INDEX idx_events_activity ON events(activity_id);
CREATE INDEX idx_event_participants_user ON event_participants(user_id);
CREATE INDEX idx_group_messages_group ON group_messages(group_id);
CREATE INDEX idx_connections_user ON connections(requester_id, receiver_id);

-- ====================================================================
-- SEED DATA (ADMIN & PUNE MEMBERS & ACTIVITIES)
-- ====================================================================

-- Users
INSERT INTO users (id, name, email, password_hash, role, photo_url, college, bio, location, status) VALUES 
('11111111-1111-1111-1111-111111111111', 'MILO Executive Admin', 'admin@milo.app', '$2a$10$e7xV6.bSg/yP/hXWq0Vw5e7yX.J3p9M5b7Vw5e7yX.J3p9M5b7V', 'ADMIN', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=400', 'COEP Tech University', 'Platform Administrator managing MILO Pune community safety.', 'Wakad, Pune', 'ACTIVE'),
('22222222-2222-2222-2222-222222222222', 'Pratik Sharma', 'pratik@milo.app', '$2a$10$e7xV6.bSg/yP/hXWq0Vw5e7yX.J3p9M5b7Vw5e7yX.J3p9M5b7V', 'USER', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=400', 'DY Patil College, Akurdi', 'Product designer living in Wakad. Love coffee meetups, casual badminton matches, and treks.', 'Wakad, Pune', 'ACTIVE'),
('33333333-3333-3333-3333-333333333333', 'Aarav Mehta', 'aarav@milo.app', '$2a$10$e7xV6.bSg/yP/hXWq0Vw5e7yX.J3p9M5b7Vw5e7yX.J3p9M5b7V', 'USER', 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&q=80&w=400', 'MIT World Peace University', 'Gamer & specialty coffee enthusiast.', 'Wakad, Pune', 'ACTIVE'),
('44444444-4444-4444-4444-444444444444', 'Ananya Deshmukh', 'ananya@milo.app', '$2a$10$e7xV6.bSg/yP/hXWq0Vw5e7yX.J3p9M5b7Vw5e7yX.J3p9M5b7V', 'USER', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=400', 'Symbiosis International', 'Always up for morning tekdi hikes or ramen in Baner.', 'Baner, Pune', 'ACTIVE');

-- User Interests
INSERT INTO user_interests (user_id, interest) VALUES
('22222222-2222-2222-2222-222222222222', 'Coffee'),
('22222222-2222-2222-2222-222222222222', 'Gaming'),
('22222222-2222-2222-2222-222222222222', 'Trekking'),
('22222222-2222-2222-2222-222222222222', 'Sports'),
('33333333-3333-3333-3333-333333333333', 'Gaming'),
('33333333-3333-3333-3333-333333333333', 'Coffee'),
('44444444-4444-4444-4444-444444444444', 'Trekking');

-- Activities Catalog
INSERT INTO activities (id, title, category, description, icon, image_url) VALUES
('a1111111-1111-1111-1111-111111111111', 'Coffee & Conversations', 'Coffee', 'Artisan cafes, relaxed conversation & social games.', '☕', 'https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?auto=format&fit=crop&q=80&w=800'),
('a2222222-2222-2222-2222-222222222222', 'Gaming & LAN Parties', 'Gaming', 'Valorant, FIFA tournaments & board game nights.', '🎮', 'https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&q=80&w=800'),
('a3333333-3333-3333-3333-333333333333', 'Badminton & Sports', 'Sports', 'Synthetic courts, double matches & friendly games.', '🏸', 'https://images.unsplash.com/photo-1626224583764-f87db24ac4ea?auto=format&fit=crop&q=80&w=800'),
('a4444444-4444-4444-4444-444444444444', 'Tekdi Hikes & Outdoors', 'Trekking', 'Sunrise trail hikes & nature walks in Pune.', '🥾', 'https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&q=80&w=800');

-- Events
INSERT INTO events (id, title, activity_id, created_by, description, location, area, event_date, event_time, total_spots, joined_spots, price, status, is_featured) VALUES
('e1111111-1111-1111-1111-111111111111', 'Saturday Coffee & Conversations ☕', 'a1111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', 'Meet new people over coffee and light icebreaker board games at Café Peter.', 'Café Peter, Wakad, Pune', 'Wakad', 'Saturday, 15 Aug', '5:00 PM', 10, 8, 0.00, 'APPROVED', TRUE),
('e2222222-2222-2222-2222-222222222222', 'Weekend Badminton Doubles 🏸', 'a3333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333333', '2 hours of doubles courts reserved at Solar Sports Arena.', 'Solar Sports Arena, Hinjewadi Ph 1', 'Hinjewadi', 'Saturday, 15 Aug', '8:00 AM', 6, 5, 150.00, 'APPROVED', FALSE);

-- Announcements
INSERT INTO announcements (id, title, content, author_id) VALUES
('b1111111-1111-1111-1111-111111111111', 'Welcome to MILO Pune Social Network! 🎉', 'Connect with members nearby in Wakad, Baner, and Hinjewadi for real-world activities.', '11111111-1111-1111-1111-111111111111');

-- App Settings
INSERT INTO app_settings (key, value) VALUES
('platform_name', 'MILO Pune Social Community'),
('default_location', 'Pune');
