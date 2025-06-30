import React from 'react';

// This is the new React 19 way!
const SEO = ({ title, description, slug }) => {
  const pageTitle = title ? `${title} | Dynamic Blog` : 'Dynamic Blog System';
  const pageDescription = description || "A personal blog built with React and Spring Boot.";
  const canonicalUrl = slug ? `http://localhost:5173/posts/${slug}` : 'http://localhost:5173';

  return (
    <>
      <title>{pageTitle}</title>
      <meta name="description" content={pageDescription} />
      <link rel="canonical" href={canonicalUrl} />
      
      {/* Open Graph tags still work the same way */}
      <meta property="og:title" content={pageTitle} />
      <meta property="og:description" content={pageDescription} />
      <meta property="og:url" content={canonicalUrl} />
      <meta property="og:site_name" content="Dynamic Blog" />
      <meta property="og:type" content={slug ? 'article' : 'website'} />
    </>
  );
};

export default SEO;