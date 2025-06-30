-- Iteration 1: Users and Posts
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    markdown_content TEXT NOT NULL,
    html_content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT', -- DRAFT, PUBLISHED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP WITH TIME ZONE,
    -- Iteration 2 Fields
    like_count INT NOT NULL DEFAULT 0,
    category_id UUID,
    -- Iteration 3 Fields
    template_id VARCHAR(50) DEFAULT 'default',
    
    CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES categories(id)
);

CREATE INDEX idx_posts_slug ON posts(slug);
CREATE INDEX idx_posts_status ON posts(status);

-- Iteration 2: Tags and Comments
CREATE TABLE tags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE post_tags (
    post_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT fk_post FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY(tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

CREATE TABLE comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    author_name VARCHAR(100),
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_comment FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- Seed a default admin user
-- Temporal Password is "password" using an online bcrypt generator
INSERT INTO users (email, password_hash) VALUES ('admin@blog.com', '$2a$10$6JeD/HyRypn6RkdIakArYuV9n57gVJv3TnZZ9PBzwAAOrFzbe7ESm');