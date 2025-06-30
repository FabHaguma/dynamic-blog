import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import apiClient from '../api/apiClient';
import SEO from '../components/SEO';
import ClassicTemplate from '../components/templates/ClassicTemplate';
import MinimalistTemplate from '../components/templates/MinimalistTemplate';

const PostDetail = () => {
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const { slug } = useParams();

  useEffect(() => {
    setLoading(true);
    apiClient.get(`/public/posts/${slug}`)
      .then(response => {
        setPost(response.data);
        setLoading(false);
      })
      .catch(err => {
        console.error("Error fetching post", err);
        setError(true);
        setLoading(false);
      });
  }, [slug]);

  const renderTemplate = () => {
    if (!post) return null;

    switch (post.templateId) {
      case 'minimalist':
        return <MinimalistTemplate post={post} />;
      case 'classic':
      default:
        return <ClassicTemplate post={post} />;
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Post not found.</div>;
  
  // Extract a short description for the meta tag
  const description = post.markdownContent.substring(0, 155) + '...';

  return (
    <>
      <SEO 
        title={post.title} 
        description={description} 
        type="article"
        slug={post.slug}
      />
      {renderTemplate()}
    </>
  );
};

export default PostDetail;