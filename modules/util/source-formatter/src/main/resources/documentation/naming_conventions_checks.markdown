# Naming Conventions Checks

Check | File Extensions | Description
----- | --------------- | -----------
CamelCaseNameCheck | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks variable names for correct use of `CamelCase`. |
ConstantNameCheck | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks that variable names of constants follow correct naming rules. |
ExceptionVariableNameCheck | .java | Validates variable names that have type `*Exception`. |
JSONNamingCheck | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks if variable names follow naming conventions. |
JSPFileNameCheck | .jsp, .jspf, .tag, .tpl or .vm | Checks if the file name of `.jsp` or `.jspf` follows the naming conventions. |
[JSPFunctionNameCheck](checks/jsp_function_name_check.markdown#jspfunctionnamecheck) | .jsp, .jspf, .tag, .tpl or .vm | Check if the names of functions in `.jsp` files follow naming conventions. |
[JSPTaglibVariableCheck](checks/jsp_taglib_variable_check.markdown#jsptaglibvariablecheck) | .jsp, .jspf, .tag, .tpl or .vm | Checks if variable names follow naming conventions. |
JavaClassNameCheck | .java | Checks if class names follow naming conventions. |
[JavaComponentActivateCheck](checks/java_component_activate_check.markdown#javacomponentactivatecheck) | .java | Checks if methods with annotation `@Activate` or `@Deactivate` follow naming conventions. |
JavaExceptionCheck | .java | Checks that variable names of exceptions in `catch` statements follow naming conventions. |
[JavaHelperUtilCheck](checks/java_helper_util_check.markdown#javahelperutilcheck) | .java | Finds incorrect use of `*Helper` or `*Util` classes. |
[JavaTestMethodAnnotationsCheck](checks/java_test_method_annotations_check.markdown#javatestmethodannotationscheck) | .java | Checks if methods with test annotations follow the naming conventions. |
[LocalFinalVariableNameCheck](https://checkstyle.sourceforge.io/config_naming.html#LocalFinalVariableName) | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks that local final variable names conform to a specified pattern. |
[LocalVariableNameCheck](https://checkstyle.sourceforge.io/config_naming.html#LocalVariableName) | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks that local, non-final variable names conform to a specified pattern. |
[MemberNameCheck](https://checkstyle.sourceforge.io/config_naming.html#MemberName) | .java, .java, .jsp, .jsp, .jspf, .jspf, .tag, .tag, .tpl, .tpl, .vm or .vm | Checks that instance variable names conform to a specified pattern. |
[MethodNameCheck](https://checkstyle.sourceforge.io/config_naming.html#MethodName) | .java, .java, .jsp, .jsp, .jspf, .jspf, .tag, .tag, .tpl, .tpl, .vm or .vm | Checks that method names conform to a specified pattern. |
MethodNamingCheck | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks that method names follow naming conventions. |
[PackageNameCheck](https://checkstyle.sourceforge.io/config_naming.html#PackageName) | .java | Checks that package names conform to a specified pattern. |
[ParameterNameCheck](https://checkstyle.sourceforge.io/config_naming.html#ParameterName) | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks that method parameter names conform to a specified pattern. |
SessionKeysCheck | .java | Checks that messages send to `SessionsErrors` or `SessionMessages` follow naming conventions. |
[StaticVariableNameCheck](https://checkstyle.sourceforge.io/config_naming.html#StaticVariableName) | .java, .java, .jsp, .jsp, .jspf, .jspf, .tag, .tag, .tpl, .tpl, .vm or .vm | Checks that static, non-final variable names conform to a specified pattern. |
StringBundlerNamingCheck | .java, .jsp, .jspf, .tag, .tpl or .vm | Checks for consistent naming on variables of type 'StringBundler'. |
[TypeNameCheck](https://checkstyle.sourceforge.io/config_naming.html#TypeName) | .java | Checks that type names conform to a specified pattern. |