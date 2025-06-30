import React from 'react';
import { Link } from 'react-router-dom';
import LikeButton from '../LikeButton';
import CommentSection from '../CommentSection';

// A different layout for demonstration
const MinimalistTemplate = ({ post }) => {
  if (!post) return null;

  return (
    <article style={{ borderLeft: '3px solid var(--accent)', paddingLeft: '1.5rem' }}>
      <p style={{ color: '#a0a0a0', margin: 0 }}>
        {new Date(post.publishedAt).toLocaleDateString()}
        {post.categoryName && ` • ${post.categoryName}`}
      </p>
      <h1 style={{ marginTop: '0.5rem' }}>{post.title}</h1>
      <div 
        className="post-content"
        dangerouslySetInnerHTML={{ __html: post.htmlContent }} 
      />
      <div style={{marginTop: '2rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          {post.tags && post.tags.map(tag => (
              <Link key={tag} to={`/tags/${tag}`} style={{ marginRight: '0.5rem', fontSize: '0.9em' }}>
                #{tag}
              </Link>
            ))}
        </div>
        <LikeButton initialLikes={post.likeCount} postId={post.id} />
      </div>
      <CommentSection postId={post.id} />
    </article>
  );
};

export default MinimalistTemplate;