# Lab 2 Web Server — Project Report

## Description of Changes
- Implemented a time retrieval service accessible through the `/time` endpoint.
- Developed a custom, more user-friendly and visually appealing error page.
- Configured SSL and HTTP/2 using a self-signed certificate.

## Technical Decisions
Unit tests were not implemented for the `/time` endpoint or its related classes due to their simplicity and limited scope.  
Instead, it was deemed more appropriate to validate the complete functionality through integration testing, ensuring that the endpoint operates correctly within the full system context.

## Learning Outcomes
- Learned how Spring Boot locates the default HTML templates for error pages and how to replace them with custom ones.
- Gained practical experience configuring SSL and HTTP/2 with a self-signed certificate, improving both security and professionalism of the application.
- Became familiar with using an automatic code formatter to ensure consistent style and readability.
- Discovered that the test client initially encountered issues connecting to HTTPS endpoints secured with self-signed certificates, due to built-in security restrictions—later resolved through a custom testing configuration.

## AI Disclosure
### AI Tools Used
- ChatGPT

### AI-Assisted Work
- Preliminary documentation generation.
- Creation of base HTML structure and JavaScript scripts.
- Development of CSS stylesheet linked to HTML.
- Translation of all project documentation.
- Review and correction of runtime errors.
- Syntax refinement and library discovery in Kotlin.
- Code review and optimization.
- Linux command assistance and general reference guidance.

### Original Work
The initial Kotlin implementation up to functional completion was entirely original.  
The development process involved designing and programming the core functionality before using AI tools for suggestions and refactoring insights.  
These recommendations were reviewed and selectively integrated, followed by manual verification and refinement to ensure code quality.  
Finally, AI was employed to generate and polish the project documentation.
