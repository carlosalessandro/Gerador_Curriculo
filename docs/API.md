# 📚 API Documentation - ATS Analysis Service

## 🌟 Overview

The ATS (Applicant Tracking System) Analysis Service provides intelligent resume analysis to optimize job applications. This service uses machine learning algorithms to evaluate resumes against job descriptions and provide actionable insights.

## 🔗 Base URL

```
https://api.cvpro.app/v1
```

## 🔐 Authentication

All API requests require authentication using Bearer tokens:

```http
Authorization: Bearer YOUR_API_KEY
```

### Getting API Key

1. Register at [CV Pro Developer Portal](https://developers.cvpro.app)
2. Create a new application
3. Copy your API key from the dashboard

## 📊 Endpoints

### 1. Analyze Resume

Analyzes a resume and provides ATS optimization suggestions.

**Endpoint**: `POST /analyze/resume`

**Headers**:
- `Content-Type: application/json`
- `Authorization: Bearer YOUR_API_KEY`

**Request Body**:
```json
{
  "resume": {
    "title": "Senior Software Engineer",
    "content": "Experienced software developer with 5+ years...",
    "sections": {
      "experience": [
        {
          "company": "Tech Corp",
          "position": "Software Engineer",
          "duration": "2020-2023",
          "description": "Developed mobile applications..."
        }
      ],
      "education": [
        {
          "institution": "University Name",
          "degree": "Bachelor of Science",
          "field": "Computer Science",
          "graduation": "2020"
        }
      ],
      "skills": ["Java", "Android", "React", "SQL"]
    },
    "contact": {
      "email": "john.doe@email.com",
      "phone": "+55 11 98765-4321",
      "linkedin": "linkedin.com/in/johndoe"
    }
  },
  "target_job": {
    "title": "Senior Android Developer",
    "description": "We are looking for an experienced Android developer...",
    "required_skills": ["Android", "Java", "Kotlin", "REST API"],
    "company": "Mobile Tech Inc",
    "location": "São Paulo, SP"
  },
  "options": {
    "language": "pt-BR",
    "industry": "technology",
    "experience_level": "senior",
    "include_suggestions": true,
    "detailed_analysis": true
  }
}
```

**Response**:
```json
{
  "success": true,
  "data": {
    "analysis_id": "ats_123456789",
    "overall_score": 85,
    "scores": {
      "keywords": 90,
      "formatting": 80,
      "content_quality": 85,
      "contact_info": 100,
      "experience_relevance": 75,
      "skills_match": 88
    },
    "status": "completed",
    "analysis_date": "2024-01-15T10:30:00Z",
    "recommendations": {
      "high_priority": [
        {
          "type": "missing_keyword",
          "message": "Add 'Kotlin' to your skills section",
          "impact": "high",
          "suggestion": "Consider adding Kotlin experience as it's mentioned 3 times in the job description"
        },
        {
          "type": "formatting",
          "message": "Improve bullet point formatting in experience section",
          "impact": "medium",
          "suggestion": "Use action verbs at the beginning of each bullet point"
        }
      ],
      "medium_priority": [
        {
          "type": "content_length",
          "message": "Resume is slightly too long",
          "impact": "low",
          "suggestion": "Consider reducing to 1-2 pages maximum"
        }
      ]
    },
    "keyword_analysis": {
      "matched_keywords": [
        {
          "keyword": "Android",
          "frequency": 8,
          "relevance": 0.95,
          "context": ["Android Developer", "Android applications"]
        },
        {
          "keyword": "Java",
          "frequency": 6,
          "relevance": 0.88,
          "context": ["Java development", "Java EE"]
        }
      ],
      "missing_keywords": [
        {
          "keyword": "Kotlin",
          "importance": 0.92,
          "reason": "Required skill mentioned in job description"
        },
        {
          "keyword": "REST API",
          "importance": 0.78,
          "reason": "Technical requirement for the position"
        }
      ]
    },
    "formatting_analysis": {
      "issues": [
        {
          "type": "inconsistent_spacing",
          "severity": "medium",
          "description": "Inconsistent spacing between sections"
        }
      ],
      "suggestions": [
        "Use consistent font sizes throughout",
        "Ensure proper alignment of dates"
      ]
    },
    "content_analysis": {
      "strengths": [
        "Strong technical experience section",
        "Clear quantifiable achievements",
        "Relevant skills listed"
      ],
      "improvements": [
        "Add more quantifiable results",
        "Include project outcomes and metrics",
        "Strengthen professional summary"
      ]
    }
  },
  "metadata": {
    "processing_time": 2.34,
    "api_version": "v1",
    "request_id": "req_987654321"
  }
}
```

### 2. Get Analysis History

Retrieves previous analysis results for a user.

**Endpoint**: `GET /analyze/history`

**Query Parameters**:
- `limit` (optional): Number of results to return (default: 10, max: 50)
- `offset` (optional): Number of results to skip (default: 0)
- `sort_by` (optional): Sort field (`date`, `score`) (default: `date`)
- `order` (optional): Sort order (`asc`, `desc`) (default: `desc`)

**Response**:
```json
{
  "success": true,
  "data": {
    "analyses": [
      {
        "analysis_id": "ats_123456789",
        "resume_title": "Senior Software Engineer",
        "target_job": "Senior Android Developer",
        "overall_score": 85,
        "analysis_date": "2024-01-15T10:30:00Z",
        "status": "completed"
      }
    ],
    "pagination": {
      "total": 25,
      "limit": 10,
      "offset": 0,
      "has_more": true
    }
  }
}
```

### 3. Get Keywords by Industry

Returns common keywords and skills for specific industries.

**Endpoint**: `GET /keywords/industry/{industry}`

**Path Parameters**:
- `industry`: Industry name (technology, healthcare, finance, etc.)

**Query Parameters**:
- `experience_level`: junior, mid, senior, executive
- `location`: Geographic location (optional)

**Response**:
```json
{
  "success": true,
  "data": {
    "industry": "technology",
    "experience_level": "senior",
    "keywords": {
      "technical": [
        {
          "keyword": "Java",
          "frequency": 0.85,
          "trend": "stable",
          "importance": 0.92
        },
        {
          "keyword": "Python",
          "frequency": 0.78,
          "trend": "increasing",
          "importance": 0.88
        }
      ],
      "soft_skills": [
        {
          "keyword": "Leadership",
          "frequency": 0.72,
          "trend": "stable",
          "importance": 0.85
        }
      ],
      "certifications": [
        {
          "keyword": "AWS Certified",
          "frequency": 0.45,
          "trend": "increasing",
          "importance": 0.78
        }
      ]
    },
    "last_updated": "2024-01-10T00:00:00Z"
  }
}
```

### 4. Resume Comparison

Compares two resumes or a resume against a job description.

**Endpoint**: `POST /compare`

**Request Body**:
```json
{
  "type": "resume_vs_job",
  "resume": {
    "content": "Resume content here..."
  },
  "job_description": {
    "content": "Job description here..."
  },
  "options": {
    "detailed_breakdown": true,
    "include_suggestions": true
  }
}
```

**Response**:
```json
{
  "success": true,
  "data": {
    "comparison_id": "comp_123456789",
    "similarity_score": 78,
    "match_percentage": 82,
    "breakdown": {
      "skills_match": 85,
      "experience_match": 75,
      "education_match": 90,
      "keywords_match": 80
    },
    "gaps": [
      {
        "type": "skill_gap",
        "description": "Missing Docker experience",
        "importance": "high"
      }
    ],
    "recommendations": [
      "Add cloud computing experience",
      "Highlight project management skills"
    ]
  }
}
```

## 📈 Rate Limits

| Plan | Requests/Minute | Requests/Hour | Requests/Day |
|------|-----------------|---------------|--------------|
| Free | 10 | 100 | 1,000 |
| Pro | 100 | 1,000 | 10,000 |
| Enterprise | 1,000 | 10,000 | 100,000 |

## 🚨 Error Handling

### HTTP Status Codes

- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request parameters
- `401 Unauthorized`: Invalid or missing API key
- `403 Forbidden`: Insufficient permissions
- `429 Too Many Requests`: Rate limit exceeded
- `500 Internal Server Error`: Server error

### Error Response Format

```json
{
  "success": false,
  "error": {
    "code": "INVALID_REQUEST",
    "message": "Invalid request parameters",
    "details": {
      "field": "resume.content",
      "reason": "Content cannot be empty"
    },
    "request_id": "req_123456789",
    "timestamp": "2024-01-15T10:30:00Z"
  }
}
```

### Common Error Codes

| Code | Description | Solution |
|------|-------------|----------|
| `INVALID_API_KEY` | API key is invalid | Check your API key |
| `RATE_LIMIT_EXCEEDED` | Too many requests | Wait and retry |
| `INVALID_CONTENT` | Resume content is invalid | Check content format |
| `SERVICE_UNAVAILABLE` | Service temporarily down | Try again later |

## 🛠️ SDKs and Libraries

### Android SDK

```gradle
implementation 'com.cvpro:ats-sdk:1.2.0'
```

```java
// Initialize SDK
ATSService atsService = ATSService.initialize("YOUR_API_KEY");

// Analyze resume
ATSAnalysisRequest request = new ATSAnalysisRequest.Builder()
    .resume(resumeContent)
    .targetJob(jobDescription)
    .build();

atsService.analyzeResume(request, new ATSCallback<ATSAnalysisResult>() {
    @Override
    public void onSuccess(ATSAnalysisResult result) {
        // Handle successful analysis
        int score = result.getOverallScore();
        List<Recommendation> recommendations = result.getRecommendations();
    }
    
    @Override
    public void onError(ATSError error) {
        // Handle error
        Log.e("ATS", "Analysis failed: " + error.getMessage());
    }
});
```

### JavaScript SDK

```bash
npm install @cvpro/ats-sdk
```

```javascript
import { ATSService } from '@cvpro/ats-sdk';

const atsService = new ATSService('YOUR_API_KEY');

try {
    const result = await atsService.analyzeResume({
        resume: resumeContent,
        targetJob: jobDescription
    });
    
    console.log('Score:', result.overallScore);
    console.log('Recommendations:', result.recommendations);
} catch (error) {
    console.error('Analysis failed:', error.message);
}
```

## 🔄 Webhooks

Configure webhooks to receive real-time notifications about analysis results.

### Setup Webhook

1. Go to your developer dashboard
2. Add webhook URL
3. Select events to subscribe to

### Webhook Events

#### analysis.completed

```json
{
  "event": "analysis.completed",
  "data": {
    "analysis_id": "ats_123456789",
    "user_id": "user_456789",
    "overall_score": 85,
    "completed_at": "2024-01-15T10:30:00Z"
  }
}
```

#### analysis.failed

```json
{
  "event": "analysis.failed",
  "data": {
    "analysis_id": "ats_123456789",
    "user_id": "user_456789",
    "error_code": "PROCESSING_ERROR",
    "failed_at": "2024-01-15T10:30:00Z"
  }
}
```

## 📊 Analytics and Monitoring

### Usage Metrics

Track your API usage and performance:

```bash
curl -H "Authorization: Bearer YOUR_API_KEY" \
     https://api.cvpro.app/v1/analytics/usage
```

**Response**:
```json
{
  "success": true,
  "data": {
    "current_month": {
      "requests": 450,
      "success_rate": 98.5,
      "average_response_time": 1.2
    },
    "last_month": {
      "requests": 380,
      "success_rate": 97.8,
      "average_response_time": 1.5
    }
  }
}
```

## 🔧 Best Practices

### 1. Request Optimization

- Use batch requests for multiple analyses
- Cache results to avoid duplicate requests
- Implement exponential backoff for retries

### 2. Error Handling

- Always check response status codes
- Implement proper error logging
- Handle rate limits gracefully

### 3. Security

- Never expose API keys in client-side code
- Use HTTPS for all requests
- Rotate API keys regularly

### 4. Performance

- Compress request payloads when possible
- Use appropriate timeout values
- Monitor response times

## 📞 Support

- **Documentation**: [docs.cvpro.app](https://docs.cvpro.app)
- **API Status**: [status.cvpro.app](https://status.cvpro.app)
- **Support Email**: api-support@cvpro.app
- **Developer Community**: [community.cvpro.app](https://community.cvpro.app)

## 🔄 Changelog

### v1.2.0 (2024-01-15)
- Added resume comparison endpoint
- Improved keyword analysis accuracy
- Added webhook support

### v1.1.0 (2024-01-01)
- Introduced industry-specific keywords
- Added batch analysis support
- Improved error handling

### v1.0.0 (2023-12-15)
- Initial API release
- Basic resume analysis
- Authentication and rate limiting

---

**For more information, visit our [Developer Portal](https://developers.cvpro.app)**
