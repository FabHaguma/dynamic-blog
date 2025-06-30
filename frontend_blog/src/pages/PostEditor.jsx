import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import apiClient from '../api/apiClient';
import ReactMarkdown from 'react-markdown';

const PostEditor = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [title, setTitle] = useState('');
  const [markdownContent, setMarkdownContent] = useState('');
  const [status, setStatus] = useState('DRAFT');
  const [categories, setCategories] = useState([]);
  const [selectedCategoryId, setSelectedCategoryId] = useState('');
  const [tags, setTags] = useState([]);
  const [tagInput, setTagInput] = useState('');
  const [templateId, setTemplateId] = useState('classic'); // Iteration 3 state

  useEffect(() => {
    apiClient.get('/public/categories').then(res => setCategories(res.data));
    if (id) {
      apiClient.get(`/admin/posts/${id}`).then(response => {
        const post = response.data;
        setTitle(post.title);
        setMarkdownContent(post.markdownContent);
        setStatus(post.status);
        setSelectedCategoryId(post.categoryId || '');
        setTags(post.tags || []);
        setTemplateId(post.templateId || 'classic'); // Set template from fetched post
      });
    }
  }, [id]);
  
  const handleTagKeyDown = (e) => {
    if (e.key === 'Enter' || e.key === ',') {
      e.preventDefault();
      const newTag = tagInput.trim();
      if (newTag && !tags.includes(newTag)) {
        setTags([...tags, newTag]);
      }
      setTagInput('');
    }
  };

  const removeTag = (tagToRemove) => {
    setTags(tags.filter(tag => tag !== tagToRemove));
  };

  const handleSave = async (newStatus) => {
    const postData = {
      title,
      markdownContent,
      status: newStatus,
      categoryId: selectedCategoryId || null,
      tags: tags,
      templateId: templateId, // Iteration 3 data
    };

    try {
      if (id) {
        await apiClient.put(`/admin/posts/${id}`, postData);
      } else {
        const response = await apiClient.post('/admin/posts', postData);
        navigate(`/admin/editor/${response.data.id}`);
        return;
      }
      alert('Post saved!');
      setStatus(newStatus);
    } catch (error) {
      console.error('Failed to save post', error);
      alert('Failed to save post.');
    }
  };

  return (
    <div>
      <h2>{id ? 'Edit Post' : 'New Post'}</h2>
      <button onClick={() => navigate('/admin/dashboard')}>Back to Dashboard</button>
      
      <div style={{marginTop: '1rem'}}>
        <label>Title</label>
        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} />
      </div>

      <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '1rem', alignItems: 'flex-end'}}>
        <div>
          <label>Category</label>
          <select value={selectedCategoryId} onChange={(e) => setSelectedCategoryId(e.target.value)} style={{width: '100%', padding: '0.8em', height: '45px'}}>
            <option value="">-- No Category --</option>
            {categories.map(cat => ( <option key={cat.id} value={cat.id}>{cat.name}</option> ))}
          </select>
        </div>
        <div>
          <label>Template</label>
          <select value={templateId} onChange={(e) => setTemplateId(e.target.value)} style={{width: '100%', padding: '0.8em', height: '45px'}}>
            <option value="classic">Classic</option>
            <option value="minimalist">Minimalist</option>
          </select>
        </div>
        <div>
          <label>Tags (press Enter)</label>
          <input type="text" value={tagInput} onChange={(e) => setTagInput(e.target.value)} onKeyDown={handleTagKeyDown} placeholder="e.g., java, react" />
        </div>
      </div>

      <div style={{display: 'flex', flexWrap: 'wrap', gap: '0.5rem', marginBottom: '1rem', minHeight: '24px'}}>
        {tags.map(tag => (
          <span key={tag} style={{backgroundColor: '#444', padding: '0.2rem 0.5rem', borderRadius: '4px'}}>
            {tag} <span onClick={() => removeTag(tag)} style={{cursor: 'pointer', color: 'red', marginLeft: '0.5rem'}}>x</span>
          </span>
        ))}
      </div>
      
      <div style={{display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginTop: '1rem'}}>
        <div>
          <h3>Markdown</h3>
          <textarea value={markdownContent} onChange={(e) => setMarkdownContent(e.target.value)} style={{ height: '60vh', resize: 'none' }} />
        </div>
        <div>
          <h3>Preview</h3>
          <div style={{border: '1px solid #3a3a3a', padding: '1em', height: '60vh', overflowY: 'auto'}} className="post-content">
            <ReactMarkdown>{markdownContent}</ReactMarkdown>
          </div>
        </div>
      </div>
      
      <div style={{marginTop: '1rem'}}>
        <button onClick={() => handleSave('DRAFT')}>Save as Draft</button>
        <button onClick={() => handleSave('PUBLISHED')} style={{marginLeft: '1rem'}}>
          {status === 'PUBLISHED' ? 'Update Published Post' : 'Publish'}
        </button>
      </div>
    </div>
  );
};

export default PostEditor;