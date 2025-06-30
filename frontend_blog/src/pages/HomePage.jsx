import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import apiClient from '../api/apiClient';
import SEO from '../components/SEO';

const HomePage = () => {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    apiClient.get('/public/posts').then(response => {
      setPosts(response.data.content);
    });
  }, []);

  return (
    <div>
      <SEO title="Home" description="Welcome to my personal blog about technology and development." />
      <h1>My Blog</h1>
      {posts
        .filter(post => post && post.slug)
        .map(post => (
        <article key={post.id} style={{ marginBottom: '2rem', borderBottom: '1px solid #3a3a3a', paddingBottom: '1rem' }}>
          <h2><Link to={`/posts/${post.slug}`}>{post.title}</Link></h2>
          <p style={{color: '#a0a0a0'}}>Published on: {new Date(post.publishedAt).toLocaleDateString()}</p>
          {post.tagNames && post.tagNames.length > 0 && (
            <div>
              {post.tagNames.map(tag => (
                <Link key={tag} to={`/tags/${tag}`} style={{ marginRight: '0.5rem', fontSize: '0.9em', backgroundColor: '#444', padding: '0.2rem 0.5rem', borderRadius: '4px' }}>
                  {tag}
                </Link>
              ))}
            </div>
          )}
        </article>
      ))}
    </div>
  );
};

export default HomePage;