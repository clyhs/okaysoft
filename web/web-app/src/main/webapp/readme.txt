http://localhost:8080/okaysoft/gis/cite/wms?service=WMS&version=1.1.0&request=GetMap&layers=cite:in101503&styles=&bbox=-158.09927368164062,17.97571563720703,-65.65161895751953,70.31949615478516&width=582&height=330&srs=EPSG:4326&format=application/openlayers


http://localhost:8080/okaysoft/gis/fconline/wms?service=WMS&version=1.1.0&request=GetMap&layers=fconline:boundaryline&styles=&bbox=113.852203202009, 22.8959660258002,113.904998803241, 22.9772903666881&width=582&height=330&srs=EPSG:4326&format=application/openlayers


SELECT ST_AsText(ST_Force_2D(ST_Envelope(ST_Estimated_Extent('bank','geom'))))

********** 错误 **********

错误: geometry_estimated_extent: null statistics for table
SQL 状态: XX000


十二月 23, 2013 9:34:04 下午 org.geotools.data.postgis.PostGISDialect getOptimizedBounds
WARNING: Failed to use ST_Estimated_Extent, falling back on envelope aggregation
org.postgresql.util.PSQLException: 错误: geometry_estimated_extent: null statistics for table
	at org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2062)
	at org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:1795)
	at org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:257)
	at org.postgresql.jdbc2.AbstractJdbc2Statement.execute(AbstractJdbc2Statement.java:479)
	at org.postgresql.jdbc2.AbstractJdbc2Statement.executeWithFlags(AbstractJdbc2Statement.java:353)
	at org.postgresql.jdbc2.AbstractJdbc2Statement.executeQuery(AbstractJdbc2Statement.java:252)
	at org.apache.commons.dbcp.DelegatingStatement.executeQuery(DelegatingStatement.java:208)
	at org.apache.commons.dbcp.DelegatingStatement.executeQuery(DelegatingStatement.java:208)
	at org.geotools.data.postgis.PostGISDialect.getOptimizedBounds(PostGISDialect.java:286)
	at org.geotools.jdbc.JDBCDataStore.getBounds(JDBCDataStore.java:1141)
	at org.geotools.jdbc.JDBCFeatureSource.getBoundsInternal(JDBCFeatureSource.java:501)
	at org.geotools.jdbc.JDBCFeatureStore.getBoundsInternal(JDBCFeatureStore.java:189)
	at org.geotools.data.store.ContentFeatureSource.getBounds(ContentFeatureSource.java:427)
	at org.geotools.data.store.ContentFeatureSource.getBounds(ContentFeatureSource.java:364)
	at org.geotools.data.SimpleFeatureSourceBridge.getBounds(SimpleFeatureSourceBridge.java:49)
	at org.vfny.geoserver.global.GeoServerFeatureSource.getBounds(GeoServerFeatureSource.java:591)
	at org.geoserver.catalog.CatalogBuilder.getNativeBounds(CatalogBuilder.java:559)
	at org.geoserver.catalog.CatalogBuilder.getNativeBounds(CatalogBuilder.java:541)
	at org.okaysoft.gis.module.action.LayerAction.create(LayerAction.java:134)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:601)
	at com.opensymphony.xwork2.DefaultActionInvocation.invokeAction(DefaultActionInvocation.java:446)
	at com.opensymphony.xwork2.DefaultActionInvocation.invokeActionOnly(DefaultActionInvocation.java:285)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:248)
	at org.apache.struts2.interceptor.debugging.DebuggingInterceptor.intercept(DebuggingInterceptor.java:256)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.DefaultWorkflowInterceptor.doIntercept(DefaultWorkflowInterceptor.java:176)
	at com.opensymphony.xwork2.interceptor.MethodFilterInterceptor.intercept(MethodFilterInterceptor.java:98)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.validator.ValidationInterceptor.doIntercept(ValidationInterceptor.java:265)
	at org.apache.struts2.interceptor.validation.AnnotationValidationInterceptor.doIntercept(AnnotationValidationInterceptor.java:68)
	at com.opensymphony.xwork2.interceptor.MethodFilterInterceptor.intercept(MethodFilterInterceptor.java:98)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.ConversionErrorInterceptor.intercept(ConversionErrorInterceptor.java:138)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.ParametersInterceptor.doIntercept(ParametersInterceptor.java:238)
	at com.opensymphony.xwork2.interceptor.MethodFilterInterceptor.intercept(MethodFilterInterceptor.java:98)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.ParametersInterceptor.doIntercept(ParametersInterceptor.java:238)
	at com.opensymphony.xwork2.interceptor.MethodFilterInterceptor.intercept(MethodFilterInterceptor.java:98)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.StaticParametersInterceptor.intercept(StaticParametersInterceptor.java:191)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at org.apache.struts2.interceptor.MultiselectInterceptor.intercept(MultiselectInterceptor.java:73)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at org.apache.struts2.interceptor.CheckboxInterceptor.intercept(CheckboxInterceptor.java:91)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at org.apache.struts2.interceptor.FileUploadInterceptor.intercept(FileUploadInterceptor.java:252)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.ModelDrivenInterceptor.intercept(ModelDrivenInterceptor.java:100)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.ScopedModelDrivenInterceptor.intercept(ScopedModelDrivenInterceptor.java:141)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.ChainingInterceptor.intercept(ChainingInterceptor.java:145)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.PrepareInterceptor.doIntercept(PrepareInterceptor.java:171)
	at com.opensymphony.xwork2.interceptor.MethodFilterInterceptor.intercept(MethodFilterInterceptor.java:98)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.I18nInterceptor.intercept(I18nInterceptor.java:176)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at org.apache.struts2.interceptor.ServletConfigInterceptor.intercept(ServletConfigInterceptor.java:164)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.AliasInterceptor.intercept(AliasInterceptor.java:193)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor.intercept(ExceptionMappingInterceptor.java:187)
	at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:242)
	at org.apache.struts2.impl.StrutsActionProxy.execute(StrutsActionProxy.java:54)
	at org.apache.struts2.dispatcher.Dispatcher.serviceAction(Dispatcher.java:544)
	at org.apache.struts2.dispatcher.ng.ExecuteOperations.executeAction(ExecuteOperations.java:77)
	at org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter.doFilter(StrutsPrepareAndExecuteFilter.java:91)
	at org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1084)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:368)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:99)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:83)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.invoke(FilterSecurityInterceptor.java:109)
	at org.springframework.security.web.access.intercept.FilterSecurityInterceptor.doFilter(FilterSecurityInterceptor.java:83)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:97)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.session.SessionManagementFilter.doFilter(SessionManagementFilter.java:100)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:78)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:54)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:35)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.authentication.www.BasicAuthenticationFilter.doFilter(BasicAuthenticationFilter.java:177)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.doFilter(AbstractAuthenticationProcessingFilter.java:187)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:105)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.context.SecurityContextPersistenceFilter.doFilter(SecurityContextPersistenceFilter.java:79)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.session.ConcurrentSessionFilter.doFilter(ConcurrentSessionFilter.java:109)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.access.channel.ChannelProcessingFilter.doFilter(ChannelProcessingFilter.java:109)
	at org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:380)
	at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:169)
	at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:237)
	at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:167)
	at org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1084)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:88)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:76)
	at org.mortbay.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1084)
	at org.mortbay.jetty.servlet.ServletHandler.handle(ServletHandler.java:360)
	at org.mortbay.jetty.security.SecurityHandler.handle(SecurityHandler.java:216)
	at org.mortbay.jetty.servlet.SessionHandler.handle(SessionHandler.java:181)
	at org.mortbay.jetty.handler.ContextHandler.handle(ContextHandler.java:726)
	at org.mortbay.jetty.webapp.WebAppContext.handle(WebAppContext.java:405)
	at org.mortbay.jetty.handler.HandlerWrapper.handle(HandlerWrapper.java:152)
	at org.mortbay.jetty.Server.handle(Server.java:324)
	at org.mortbay.jetty.HttpConnection.handleRequest(HttpConnection.java:505)
	at org.mortbay.jetty.HttpConnection$RequestHandler.headerComplete(HttpConnection.java:828)
	at org.mortbay.jetty.HttpParser.parseNext(HttpParser.java:514)
	at org.mortbay.jetty.HttpParser.parseAvailable(HttpParser.java:211)
	at org.mortbay.jetty.HttpConnection.handle(HttpConnection.java:380)
	at org.mortbay.jetty.bio.SocketConnector$Connection.run(SocketConnector.java:228)
	at org.mortbay.thread.BoundedThreadPool$PoolThread.run(BoundedThreadPool.java:450)
	
	
	
	
	java.lang.ClassFormatError: Absent Code attribute in method that is not native or abstract in class file javax/persistence/Persistence
	at java.lang.ClassLoader.defineClass1(Native Method) ~[na:1.7.0_09]
	at java.lang.ClassLoader.defineClass(ClassLoader.java:791) ~[na:1.7.0_09]
	at java.security.SecureClassLoader.defineClass(SecureClassLoader.java:142) ~[na:1.7.0_09]
	at java.net.URLClassLoader.defineClass(URLClassLoader.java:449) ~[na:1.7.0_09]
	at java.net.URLClassLoader.access$100(URLClassLoader.java:71) ~[na:1.7.0_09]
	at java.net.URLClassLoader$1.run(URLClassLoader.java:361) ~[na:1.7.0_09]
	at java.net.URLClassLoader$1.run(URLClassLoader.java:355) ~[na:1.7.0_09]
	at java.security.AccessController.doPrivileged(Native Method) ~[na:1.7.0_09]
	at java.net.URLClassLoader.findClass(URLClassLoader.java:354) ~[na:1.7.0_09]
	at org.mortbay.jetty.webapp.WebAppClassLoader.loadClass(WebAppClassLoader.java:366) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.jetty.webapp.WebAppClassLoader.loadClass(WebAppClassLoader.java:337) ~[jetty-6.1.8.jar:6.1.8]
	at org.hibernate.validator.internal.util.privilegedactions.LoadClass.loadNonValidatorClass(LoadClass.java:87) ~[hibernate-validator-4.3.1.Final.jar:4.3.1.Final]
	at org.hibernate.validator.internal.util.privilegedactions.LoadClass.run(LoadClass.java:53) ~[hibernate-validator-4.3.1.Final.jar:4.3.1.Final]
	at org.hibernate.validator.internal.util.ReflectionHelper.loadClass(ReflectionHelper.java:129) ~[hibernate-validator-4.3.1.Final.jar:4.3.1.Final]
	at org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver.detectJPA(DefaultTraversableResolver.java:71) ~[hibernate-validator-4.3.1.Final.jar:4.3.1.Final]
	at org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver.<init>(DefaultTraversableResolver.java:61) ~[hibernate-validator-4.3.1.Final.jar:4.3.1.Final]
	at org.hibernate.validator.internal.engine.ConfigurationImpl.<init>(ConfigurationImpl.java:74) ~[hibernate-validator-4.3.1.Final.jar:4.3.1.Final]
	at org.hibernate.validator.HibernateValidator.createGenericConfiguration(HibernateValidator.java:41) ~[hibernate-validator-4.3.1.Final.jar:4.3.1.Final]
	at javax.validation.Validation$GenericBootstrapImpl.configure(Validation.java:276) ~[javaee-api-7.0.jar:na]
	at javax.validation.Validation.buildDefaultValidatorFactory(Validation.java:110) ~[javaee-api-7.0.jar:na]
	at org.hibernate.cfg.beanvalidation.TypeSafeActivator.getValidatorFactory(TypeSafeActivator.java:380) ~[hibernate-core-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.cfg.beanvalidation.TypeSafeActivator.applyDDL(TypeSafeActivator.java:109) ~[hibernate-core-3.6.10.Final.jar:3.6.10.Final]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.7.0_09]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57) ~[na:1.7.0_09]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.7.0_09]
	at java.lang.reflect.Method.invoke(Method.java:601) ~[na:1.7.0_09]
	at org.hibernate.cfg.beanvalidation.BeanValidationActivator.applyDDL(BeanValidationActivator.java:118) ~[hibernate-core-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.cfg.Configuration.applyBeanValidationConstraintsOnDDL(Configuration.java:1704) ~[hibernate-core-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.cfg.Configuration.applyConstraintsToDDL(Configuration.java:1654) ~[hibernate-core-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.cfg.Configuration.secondPassCompile(Configuration.java:1445) ~[hibernate-core-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.cfg.Configuration.buildMappings(Configuration.java:1375) ~[hibernate-core-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.ejb.Ejb3Configuration.buildMappings(Ejb3Configuration.java:1519) ~[hibernate-entitymanager-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.ejb.EventListenerConfigurator.configure(EventListenerConfigurator.java:193) ~[hibernate-entitymanager-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.ejb.Ejb3Configuration.configure(Ejb3Configuration.java:1100) ~[hibernate-entitymanager-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.ejb.Ejb3Configuration.configure(Ejb3Configuration.java:689) ~[hibernate-entitymanager-3.6.10.Final.jar:3.6.10.Final]
	at org.hibernate.ejb.HibernatePersistence.createContainerEntityManagerFactory(HibernatePersistence.java:73) ~[hibernate-entitymanager-3.6.10.Final.jar:3.6.10.Final]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:225) ~[spring-orm-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:308) ~[spring-orm-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1477) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1417) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:519) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:456) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:291) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:288) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:190) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor.findDefaultEntityManagerFactory(PersistenceAnnotationBeanPostProcessor.java:529) ~[spring-orm-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor.findEntityManagerFactory(PersistenceAnnotationBeanPostProcessor.java:495) ~[spring-orm-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor$PersistenceElement.resolveExtendedEntityManager(PersistenceAnnotationBeanPostProcessor.java:682) ~[spring-orm-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor$PersistenceElement.getResourceToInject(PersistenceAnnotationBeanPostProcessor.java:628) ~[spring-orm-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.annotation.InjectionMetadata$InjectedElement.inject(InjectionMetadata.java:147) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:84) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor.postProcessPropertyValues(PersistenceAnnotationBeanPostProcessor.java:338) ~[spring-orm-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1074) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:517) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:456) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:291) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:288) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:190) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:580) ~[spring-beans-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:895) ~[spring-context-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:425) ~[spring-context-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.web.context.ContextLoader.createWebApplicationContext(ContextLoader.java:276) ~[spring-web-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.springframework.web.context.ContextLoader.initWebApplicationContext(ContextLoader.java:197) ~[spring-web-3.0.5.RELEASE.jar:3.0.5.RELEASE]
	at org.okaysoft.core.spring.OkayContextLoaderListener.contextInitialized(OkayContextLoaderListener.java:33) ~[classes/:na]
	at org.mortbay.jetty.handler.ContextHandler.startContext(ContextHandler.java:540) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.jetty.servlet.Context.startContext(Context.java:135) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.jetty.webapp.WebAppContext.startContext(WebAppContext.java:1220) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.jetty.handler.ContextHandler.doStart(ContextHandler.java:510) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.jetty.webapp.WebAppContext.doStart(WebAppContext.java:448) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.component.AbstractLifeCycle.start(AbstractLifeCycle.java:39) ~[jetty-util-6.1.8.jar:6.1.8]
	at org.mortbay.jetty.handler.HandlerWrapper.doStart(HandlerWrapper.java:130) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.jetty.Server.doStart(Server.java:222) ~[jetty-6.1.8.jar:6.1.8]
	at org.mortbay.component.AbstractLifeCycle.start(AbstractLifeCycle.java:39) ~[jetty-util-6.1.8.jar:6.1.8]
	at org.okaysoft.Start.main(Start.java:64) [test-classes/:na]
	