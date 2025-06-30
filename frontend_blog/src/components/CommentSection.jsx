import React, { useState, useEffect } from 'react';
import apiClient from '../api/apiClient';

const CommentSection = ({ postId }) => {
  const [comments, setComments] = useState([]);
  const [author, setAuthor] = useState('');
  const [content, setContent] = useState('');

  useEffect(() => {
    fetchComments();
  }, [postId]);

  const fetchComments = () => {
    apiClient.get(`/public/posts/${postId}/comments`)
      .then(response => setComments(response.data))
      .catch(err => console.error("Failed to fetch comments", err));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!content.trim()) return;

    const newComment = { authorName: author, content };
    apiClient.post(`/public/posts/${postId}/comments`, newComment)
      .then(response => {
        setComments([response.data, ...comments]);
        setAuthor('');
        setContent('');
      })
      .catch(err => console.error("Failed to post comment", err));
  };

  return (
    <div style={{ marginTop: '3rem', borderTop: '1px solid #3a3a3a', paddingTop: '2rem' }}>
      <h3>Comments ({comments.length})</h3>
      
      {/* Comment Form */}
      <form onSubmit={handleSubmit} style={{ marginBottom: '2rem' }}>
        <input 
          type="text" 
          placeholder="Your name (optional)" 
          value={author} 
          onChange={e => setAuthor(e.target.value)}
        />
        <textarea 
          placeholder="Write a comment..." 
          value={content} 
          onChange={e => setContent(e.target.value)} 
          required
          rows="4"
        />
        <button type="submit">Post Comment</button>
      </form>
      
      {/* Comments List */}
      <div>
        {comments.map(comment => (
          <div key={comment.id} style={{ border: '1px solid #3a3a3a', borderRadius: '8px', padding: '1rem', marginBottom: '1rem' }}>
            <p><strong>{comment.authorName}</strong> <span style={{fontSize: '0.8em', color: '#a0a0a0'}}>on {new Date(comment.createdAt).toLocaleDateString()}</span></p>
            <p>{comment.content}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CommentSection;