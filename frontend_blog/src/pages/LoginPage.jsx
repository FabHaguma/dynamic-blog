import React, { useState } from 'react';
import apiClient from '../api/apiClient';
import { useAuth } from '../components/AuthProvider';

const LoginPage = () => {
  const [email, setEmail] = useState('admin@blog.com');
  const [password, setPassword] = useState('password');
  const [error, setError] = useState('');
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await apiClient.post('/auth/login', { email, password });
      login(response.data.token);
    } catch (err) {
      setError('Invalid email or password: ' + err.message);
    }
  };

  return (
    <div>
      <h2>Admin Login</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} required />
        </div>
        <div>
          <label>Password</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </div>
        {error && <p style={{color: 'red'}}>{error}</p>}
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default LoginPage;