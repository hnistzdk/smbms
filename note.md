${pageContext.request.contextPath}用于解决使用相对路径时出现的问题，
它的作用是取出所部署项目的名字。在form表单提交、<a>标记和Ajax的url中经常使用。

使用${pageContext.request.contextPath}出现问题的话，需要在jsp页面增加此命令
<%@page isELIgnored="false"%> 不忽略EL表达式

用户查看功能：提供userCode userName gender birthday phone address userRoleName的显示