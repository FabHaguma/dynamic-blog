import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';
import { useAuth } from '../components/AuthProvider';

const AdminDashboard = () => {
  const [posts, setPosts] = useState([]);
  const { logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    fetchPosts();
  }, []);

  const fetchPosts = () => {
    apiClient.get('/admin/posts').then(response => {
      setPosts(response.data.content);
    });
  };

  return (
    <div>
      <h1>Admin Dashboard</h1>
      <button onClick={() => navigate('/admin/editor')} style={{marginBottom: '1rem'}}>New Post</button>
      <button onClick={logout} style={{marginLeft: '1rem', backgroundColor: '#555'}}>Logout</button>
      <table style={{width: '100%', borderCollapse: 'collapse'}}>
        <thead>
          <tr style={{borderBottom: '1px solid white'}}>
            <th style={{textAlign: 'left', padding: '8px'}}>Title</th>
            <th style={{textAlign: 'left', padding: '8px'}}>Status</th>
            <th style={{textAlign: 'left', padding: '8px'}}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {posts.map(post => (
            <tr key={post.id} style={{borderBottom: '1px solid #3a3a3a'}}>
              <td style={{padding: '8px'}}>{post.title}</td>
              <td style={{padding: '8px'}}>{post.status}</td>
              <td style={{padding: '8px'}}>
                <Link to={`/admin/editor/${post.id}`}>Edit</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminDashboard;