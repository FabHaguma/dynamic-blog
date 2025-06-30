import React from 'react';
import { Link } from 'react-router-dom';
import LikeButton from '../LikeButton';
import CommentSection from '../CommentSection';

// This is essentially the layout from our previous PostDetail page
const ClassicTemplate = ({ post }) => {
  if (!post) return null;

  return (
    <article>
      <h1>{post.title}</h1>
      <div style={{ color: '#a0a0a0', marginBottom: '1rem' }}>
        <span>Published: {new Date(post.publishedAt).toLocaleDateString()}</span>
        {post.categoryName && <span> | Category: {post.categoryName}</span>}
      </div>
      <div 
        className="post-content"
        dangerouslySetInnerHTML={{ __html: post.htmlContent }} 
      />
      {post.tags && post.tags.length > 0 && (
        <div style={{ marginTop: '2rem' }}>
          <strong>Tags:</strong>
          {post.tags.map(tag => (
            <Link key={tag} to={`/tags/${tag}`} style={{ marginLeft: '0.5rem', backgroundColor: '#444', padding: '0.2rem 0.5rem', borderRadius: '4px', textDecoration: 'none' }}>
              {tag}
            </Link>
          ))}
        </div>
      )}
      <div style={{marginTop: '2rem'}}>
        <LikeButton initialLikes={post.likeCount} postId={post.id} />
      </div>
      <CommentSection postId={post.id} />
    </article>
  );
};

export default ClassicTemplate;