import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import PostDetail from './pages/PostDetail';
import LoginPage from './pages/LoginPage';
import AdminDashboard from './pages/AdminDashboard';
import PostEditor from './pages/PostEditor';
import TagPostsPage from './pages/TagPostsPage';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider } from './components/AuthProvider';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<HomePage />} />
        <Route path="/posts/:slug" element={<PostDetail />} />
        <Route path="/tags/:slug" element={<TagPostsPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Admin Routes */}
        <Route path="/admin" element={<ProtectedRoute />}>
          <Route path="dashboard" element={<AdminDashboard />} />
          <Route path="editor" element={<PostEditor />} />
          <Route path="editor/:id" element={<PostEditor />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
}

export default App;