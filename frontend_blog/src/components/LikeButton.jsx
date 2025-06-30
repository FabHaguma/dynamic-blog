import React, { useState } from 'react';
import apiClient from '../api/apiClient';

const LikeButton = ({ initialLikes, postId }) => {
  const [likes, setLikes] = useState(initialLikes);
  const [liked, setLiked] = useState(false);

  const handleLike = () => {
    if (!liked) {
      apiClient.post(`/public/posts/${postId}/like`)
        .then(() => {
          setLikes(likes + 1);
          setLiked(true);
        })
        .catch(err => console.error("Failed to like post", err));
    }
  };

  return (
    <button onClick={handleLike} disabled={liked} style={{cursor: liked ? 'not-allowed' : 'pointer'}}>
      👍 Like ({likes})
    </button>
  );
};

export default LikeButton;