package com.longshihan.broca_compiler;

import com.google.auto.service.AutoService;
import com.longshihan.broca_annotation.LocalBind;
import com.longshihan.broca_compiler.proxy.ClassValidator;
import com.longshihan.broca_compiler.proxy.LocalProxyInfo;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 *
 */
@AutoService(Processor.class)
public class LocalBindViewInjectProcessor extends AbstractProcessor {
    private Messager messager;
    private Elements elementUtils;
    private Map<String, LocalProxyInfo> mProxyMap = new HashMap<String, LocalProxyInfo>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager=processingEnv.getMessager();
        elementUtils=processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(LocalBind.class.getCanonicalName());
        return supportTypes;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mProxyMap.clear();
        Set<? extends Element> elesWithBind = roundEnv.getElementsAnnotatedWith(LocalBind.class);
        for (Element element : elesWithBind) {
            checkAnnotationValid(element, LocalBind.class);

            ExecutableElement variableElement = (ExecutableElement) element;
            //class type
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();

            //full class name
            String fqClassName = classElement.getQualifiedName().toString();

            LocalProxyInfo proxyInfo = mProxyMap.get(fqClassName);
            if (proxyInfo == null) {
                proxyInfo = new LocalProxyInfo(elementUtils, classElement);
                mProxyMap.put(fqClassName, proxyInfo);
            }

            LocalBind bindAnnotation = variableElement.getAnnotation(LocalBind.class);
            proxyInfo.setValus(bindAnnotation.value());
            proxyInfo.injectVariables.put(fqClassName, variableElement);
        }
        for (String key : mProxyMap.keySet()) {
            LocalProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(proxyInfo.getTypeElement(),
                        "Unable to write injector for type %s: %s",
                        proxyInfo.getTypeElement(), e.getMessage());
            }

        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private boolean checkAnnotationValid(Element annotatedElement, Class clazz) {
        if (annotatedElement.getKind() != ElementKind.FIELD) {
            messager.printMessage(Diagnostic.Kind.NOTE, "use ExecutableElement");
            return false;
        }
        if (ClassValidator.isPrivate(annotatedElement)) {
            error(annotatedElement, "%s() must can not be private.", annotatedElement
                    .getSimpleName());
            return false;
        }

        return true;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }
}
