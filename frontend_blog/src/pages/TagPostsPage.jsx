import React, { useState, useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import apiClient from '../api/apiClient';

const TagPostsPage = () => {
  const [posts, setPosts] = useState([]);
  const { slug } = useParams();

  useEffect(() => {
    apiClient.get(`/public/tags/${slug}/posts`).then(response => {
      setPosts(response.data.content);
    });
  }, [slug]);

  return (
    <div>
      <h1>Posts tagged with "{slug}"</h1>
      <Link to="/">← Back to all posts</Link>
      <div style={{marginTop: '2rem'}}>
        {posts.map(post => (
          <article key={post.id} style={{ marginBottom: '2rem', borderBottom: '1px solid #3a3a3a', paddingBottom: '1rem' }}>
            <h2><Link to={`/posts/${post.slug}`}>{post.title}</Link></h2>
            <p>Published on: {new Date(post.publishedAt).toLocaleDateString()}</p>
          </article>
        ))}
      </div>
    </div>
  );
};

export default TagPostsPage;