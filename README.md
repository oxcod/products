# Product Management System

A full-stack product management application built with Spring Boot and Kotlin, featuring modern server-side rendering with WebAwesome UI components.

## 🚀 Technology Stack

### Core Frameworks
- **JVM**: 24
- **Kotlin**: 2.2.0
- **Spring Boot**: 3.5.3
- **Spring Dependency Management**: 1.1.7
- **Gradle**: Kotlin DSL

### Database
- **PostgreSQL**: Latest version (via runtimeOnly dependency)
- **Flyway Core**: Version managed by Spring Boot 3.5.3
- **Flyway PostgreSQL**: Version managed by Spring Boot 3.5.3
- **Spring JDBC**: Via spring-boot-starter-jdbc

### Frontend Technologies
- **Thymeleaf**: Version managed by Spring Boot 3.5.3
- **HTMX**: 2.0.6
- **HTMX Spring Boot Thymeleaf**: 4.0.1
- **WebAwesome**: 3.0.0-beta.1 (evolution of Shoelace)

### Development Tools
- **Spring Boot DevTools**: Auto-reload for development
- **Jackson Kotlin Module**: JSON serialization support
- **Kotlin Reflect**: Reflection support
- **JUnit 5**: Testing framework

## 📋 Features

### Core Functionality
- ✅ **Product Management**: Add, edit, delete products with HTMX no-refresh interactions
- ✅ **Real-time Search**: Live search with 300ms debounce and result count display
- ✅ **Advanced Sorting**: Sortable columns (ID, Title) with ASC/DESC indicators
- ✅ **Smart Pagination**: 4 records per page with sort state preservation
- ✅ **Variant Support**: Display product variants with availability status
- ✅ **Data Sync**: Scheduled job to sync products from external API

### Modern UI/UX
- ✅ **WebAwesome Components**: Complete UI built with WebAwesome 3.0 design system
- ✅ **Modal Dialogs**: WebAwesome Dialog components for edit and delete confirmation
- ✅ **Responsive Design**: Mobile-friendly layout with proper spacing and alignment
- ✅ **Visual Feedback**: Success/danger button variants and loading states
- ✅ **Accessibility**: Proper ARIA labels and keyboard navigation

### Technical Excellence
- ✅ **Server-Side Rendering**: Thymeleaf templates with SEO optimization
- ✅ **Security**: Parameterized queries, CSRF protection, XSS prevention
- ✅ **Performance**: Database indexes, efficient pagination, batch operations
- ✅ **Data Integrity**: Foreign key constraints, cascade operations
- ✅ **Type Safety**: Kotlin null safety, data classes, type-safe repositories

## 🏗️ Project Structure

```
products/
├── build.gradle.kts                    # Gradle build configuration
├── analyze_products.py                 # Data analysis script
├── src/main/
│   ├── java/com/oxcod/products/
│   │   ├── ProductsApplication.kt      # Main application entry
│   │   ├── config/
│   │   │   └── JdbcConfig.kt          # Database configuration
│   │   ├── controller/               
│   │   │   └── ProductController.kt    # MVC controller with PUT support
│   │   ├── dto/
│   │   │   └── FammeDto.kt            # External API data transfer objects
│   │   ├── model/                    
│   │   │   ├── Product.kt             # Product entity
│   │   │   └── Variant.kt             # Variant entity
│   │   ├── repository/               
│   │   │   ├── ProductRepository.kt   # Product repository with search/sort
│   │   │   └── VariantRepository.kt   # Variant repository
│   │   └── service/                      
│   │       └── ProductSyncService.kt  # External API sync service
│   └── resources/
│       ├── application.yml             # Application configuration
│       ├── db/migration/              # Flyway migration scripts
│       │   ├── V1__init.sql          # Initial table structure
│       │   ├── V2__add_timestamps.sql # Timestamp fields with triggers
│       │   └── V3__add_sorting_indexes.sql # Performance indexes
│       ├── static/
│       │   └── js/
│       │       └── app.js             # WebAwesome dialog handlers
│       └── templates/                 # Thymeleaf templates
│           ├── index.html             # Main page with WebAwesome layout
│           ├── search.html            # Dedicated search page
│           ├── archived/              # Original implementation backup
│           └── fragments/             # Reusable WebAwesome components
│               ├── product-table.html # Product table with dialogs
│               ├── search-results.html # Search results fragment
│               └── edit-form.html     # Edit form with PUT support
```

## 🎯 Implementation Timeline

### v1.0 - WebAwesome Frontend Rewrite (Latest)
**Complete UI modernization with WebAwesome 3.0 components**

#### Frontend Transformation
- ✅ **Component Migration**: Replace all custom CSS with WebAwesome utility classes
- ✅ **Dialog System**: Implement WebAwesome Dialog for edit and delete confirmation
- ✅ **Layout Fixes**: Proper spacing using wa-divider components and flexbox
- ✅ **Visual Hierarchy**: Add Product form with distinct background styling
- ✅ **Color Consistency**: Unified green buttons (variant="success") across forms

#### Technical Improvements
- ✅ **PUT Endpoint**: Add proper REST endpoint for edit form HTMX requests
- ✅ **Event Handling**: Global HTMX event listeners for dialog management
- ✅ **URL Resolution**: Fix Thymeleaf URL parsing in HTMX attributes using th:attr
- ✅ **Pagination Layout**: Horizontal pagination using flexbox instead of vertical stacking
- ✅ **Form Validation**: Prevent dialog closure during input while maintaining auto-close on success

#### Developer Experience
- ✅ **Code Organization**: Archive original implementation for reference
- ✅ **Maintainability**: Eliminate custom CSS in favor of design system
- ✅ **Debugging**: Improved error handling and console logging
- ✅ **Documentation**: Comprehensive commit messages and code comments

### v0.3 - Advanced Features (Previous)
1. **Search & Sort Integration**
   - Real-time search with debounce
   - Sortable columns with state persistence
   - Combined search and sort functionality

2. **Enhanced UX**
   - Modal edit windows (replaced standalone page)
   - Two-click delete confirmation
   - Auto-refresh after operations

3. **Performance Optimization**
   - Database indexes for search/sort
   - Efficient pagination with LIMIT/OFFSET
   - Batch operations for data sync

### v0.2 - Form & Display Improvements
- Fixed Shoelace component form submission issues
- Added timestamp display with PostgreSQL triggers
- Version upgrades (Kotlin 2.2.0, Spring Boot 3.5.3)

### v0.1 - Foundation
- Spring Boot + Kotlin setup
- PostgreSQL with Flyway migrations
- Basic CRUD operations
- External API integration (famme.no)

## 🛠️ Development Setup

### Prerequisites
- **Java 24** or higher
- **PostgreSQL** database
- **Gradle** (wrapper included)

### Quick Start
1. **Clone and setup database**:
   ```bash
   git clone https://github.com/oxcod/products.git
   cd products
   createdb products
   ```

2. **Configure application**:
   ```bash
   cp src/main/resources/application.yml.example src/main/resources/application.yml
   # Edit database credentials if needed
   ```

3. **Run application**:
   ```bash
   ./gradlew bootRun
   ```

4. **Access application**:
   - Main interface: http://localhost:9001
   - Search page: http://localhost:9001/search

### Database Schema
The application uses Flyway migrations to manage database schema:
- **V1**: Initial products and variants tables with foreign keys
- **V2**: Timestamp fields with PostgreSQL triggers for auto-update
- **V3**: Performance indexes for search and sorting

## 🔧 Key Technical Features

### 1. WebAwesome Design System
- **Components**: Cards, buttons, inputs, dialogs, alerts, badges
- **Utility Classes**: Spacing, colors, typography, layout
- **Icons**: Comprehensive icon set with semantic usage
- **Theming**: Consistent design tokens and CSS variables

### 2. HTMX Integration
- **No-Refresh Updates**: All operations update content without page reload
- **Event Handling**: Custom JavaScript for dialog lifecycle management
- **Error Handling**: Graceful error states and user feedback
- **Progressive Enhancement**: Works with JavaScript disabled

### 3. Database Design
- **Relational Model**: Products with one-to-many variants
- **Automatic Timestamps**: PostgreSQL triggers for created_at/updated_at
- **Performance Indexes**: Optimized for search and sort operations
- **Data Integrity**: Foreign key constraints and cascade operations

### 4. Security & Performance
- **SQL Injection Prevention**: Parameterized queries throughout
- **XSS Protection**: Thymeleaf auto-escaping
- **CSRF Protection**: Spring Security default configuration
- **Caching Strategy**: ID-based sync to prevent data loss

## 🚦 API Endpoints

### Product Management
- `GET /` - Main product interface
- `GET /products` - Load products (HTMX, supports pagination/search/sort)
- `POST /products` - Add new product
- `GET /products/{id}/edit` - Load edit form (HTMX)
- `PUT /products/{id}` - Update product (HTMX)
- `DELETE /products/{id}` - Delete product (HTMX)

### Search Interface
- `GET /search` - Dedicated search page
- `GET /search/products` - Search products (HTMX)

## 🎨 UI Components

### WebAwesome Component Usage
```html
<!-- Search with icon -->
<wa-input type="text" size="large" class="wa-w-full">
    <wa-icon name="search" slot="prefix"></wa-icon>
</wa-input>

<!-- Action buttons -->
<wa-button variant="success" size="small">
    <wa-icon name="plus" slot="prefix"></wa-icon>
    Add Product
</wa-button>

<!-- Delete confirmation dialog -->
<wa-dialog id="delete-confirm-dialog" label="Confirm Delete">
    <wa-icon name="triangle-exclamation" class="wa-text-danger"></wa-icon>
    <!-- Dialog content -->
</wa-dialog>
```

### Layout Patterns
- **Card Containers**: `wa-card` for content sections
- **Flexbox Layouts**: `wa-flex wa-gap-*` for spacing
- **Responsive Design**: `wa-w-full wa-max-w-*` for containers
- **Visual Separation**: `wa-divider` components between sections

## 🔄 Development Workflow

### Code Organization
- **Controllers**: Handle HTTP requests and model binding
- **Repositories**: Database access with JdbcClient/JdbcTemplate
- **Services**: Business logic and external API integration
- **Templates**: Thymeleaf fragments for reusable components

### Testing Strategy
- **Unit Tests**: Repository and service layer testing
- **Integration Tests**: Full application context testing
- **Manual Testing**: Browser-based UI testing

### Version Control
- **Feature Branches**: Separate branches for major features
- **Commit Standards**: Conventional commits with detailed descriptions
- **Code Review**: Pull request workflow for quality assurance

## 🚀 Deployment

### Production Considerations
- **Environment Variables**: Database credentials, API keys
- **Logging**: Structured logging for monitoring
- **Health Checks**: Spring Actuator endpoints
- **Docker**: Containerization for deployment

### Monitoring
- **Application Metrics**: Spring Boot Actuator
- **Database Performance**: PostgreSQL query analysis
- **Frontend Performance**: HTMX request timing

## 🔮 Future Enhancements

### Short Term
- **Bulk Operations**: Multi-select for batch delete
- **Data Export**: CSV/Excel export functionality
- **Advanced Search**: Multi-field search filters
- **Variant Management**: Direct variant editing interface

### Long Term
- **API Layer**: REST API for external integrations
- **Caching**: Redis integration for performance
- **Authentication**: User management and permissions
- **Analytics**: Usage statistics and reporting

## 📝 Technical Notes

### Critical Implementation Details

#### WebAwesome Integration
The frontend was completely rewritten to use WebAwesome 3.0 components, eliminating all custom CSS in favor of the design system:

```kotlin
// Controller supports both POST and PUT for form compatibility
@PostMapping("/products/{id}/update") // Traditional form submission
@PutMapping("/products/{id}")         // HTMX requests
```

#### HTMX Event Management
Global event listeners handle dialog lifecycle to prevent conflicts:

```javascript
document.body.addEventListener('htmx:afterSwap', function(event) {
    if (event.target.id === 'product-table-fragment') {
        // Close edit dialog after successful update
        const editDialog = document.getElementById('edit-dialog');
        if (editDialog && editDialog.open) {
            editDialog.open = false;
        }
    }
});
```

#### Data Sync Protection
The sync service uses ID caching to prevent data loss:

```kotlin
private val syncedProductIds = ConcurrentHashMap.newKeySet<Long>()

// Only sync new products, never delete existing data
if (!syncedProductIds.contains(productId)) {
    // Process new product
}
```

## 🤝 Contributing

This project demonstrates modern Spring Boot development with WebAwesome UI components. The codebase follows Spring Boot best practices and maintains clear separation of concerns for easy maintenance and extension.

### Development Guidelines
- Follow Kotlin coding conventions
- Use WebAwesome components instead of custom CSS
- Maintain HTMX compatibility for all interactions
- Write comprehensive commit messages
- Test all user interactions thoroughly

---

**Project Status**: ✅ Production Ready  
**Latest Version**: v1.0  
**Repository**: https://github.com/oxcod/products (private)  
**Deployment**: localhost:9001