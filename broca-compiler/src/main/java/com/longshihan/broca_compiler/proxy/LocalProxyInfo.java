package com.longshihan.broca_compiler.proxy;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by longshihan on 2017/12/17.
 */

public class LocalProxyInfo {
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;

    public Map<String, ExecutableElement> injectVariables = new HashMap<>();

    public static final String PROXY = "$BroadcastInject";
    private String[] valus;

    public LocalProxyInfo(Elements elementUtils, TypeElement classElement) {
        this.typeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        //classname
        String className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        System.out.println("log:" + packageName + ":" + className);
        this.proxyClassName = className + PROXY;
    }


    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import com.longshihan.broca_api.*;\n");
        builder.append("import android.content.IntentFilter;\n");
        builder.append("import android.support.v4.content.LocalBroadcastManager;\n");
        builder.append("import android.content.BroadcastReceiver;\n");
        builder.append("import android.content.Context;\n");
        builder.append("import android.content.Intent;\n");
        builder.append("import android.support.annotation.UiThread;\n");
        builder.append("import ").append(typeElement.getQualifiedName()).append(" ;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName).append(" implements Unbinder");
        builder.append(" {\n");
        builder.append("private LocalReceiver localReceiver;\n");
        builder.append("private LocalBroadcastManager localBroadcastManager;\n");
        builder.append(" private IntentFilter intentFilter;\n");
        builder.append(" private ").append(typeElement.getQualifiedName()).append("  host;\n");
        generateCMethods(builder);
        generateMethods(builder);
        generateDMethods(builder);
        builder.append('\n');
        builder.append("}\n");
        return builder.toString();
    }


    private void generateCMethods(StringBuilder builder) {
        builder.append("@UiThread\n ");
        builder.append("public  "+proxyClassName+"(" + typeElement.getQualifiedName() + " host) {\n");
        builder.append("localBroadcastManager = LocalBroadcastManager.getInstance(host);\n");
        builder.append("intentFilter = new IntentFilter();\n");
        builder.append("this.host=host;\n");
        for (String value : valus) {
            builder.append("intentFilter.addAction(\"").append(value).append("\");\n");
        }
        builder.append("localReceiver = new LocalReceiver();\n");
        builder.append("localBroadcastManager.registerReceiver(localReceiver, intentFilter);\n");
        builder.append("  }\n");
    }


    private void generateMethods(StringBuilder builder) {
        builder.append("public class LocalReceiver extends BroadcastReceiver{\n");
        builder.append(" @Override\n");
        builder.append("public void onReceive(Context context, Intent intent) {\n");
        for (String id : injectVariables.keySet()) {
            ExecutableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            builder.append("host.").append(name).append("(").append("intent").append(");\n");
            builder.append("}\n");
        }
        builder.append("}\n");
    }

    private void generateDMethods(StringBuilder builder) {
        builder.append(" @Override\n");
        builder.append("public void unbind(){\n");
        builder.append(" localBroadcastManager.unregisterReceiver(localReceiver);\n");
        builder.append("}\n");
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setValus(String[] valus) {
        this.valus = valus;
    }
}
