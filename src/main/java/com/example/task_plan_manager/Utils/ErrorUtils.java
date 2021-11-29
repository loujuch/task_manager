package com.example.task_plan_manager.Utils;

import javafx.scene.control.Alert;

public class ErrorUtils {

    private final static String TAG="com.example.task_plan_manager.Utils.ErrorUtils.";

    public static void NullPointerInputError(String s) {
        if(s==null)s=TAG+"NullPointerInputError";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("一些错误发生了！");
        alert.setHeaderText("预期外的空指针传入！");
        alert.setContentText("传入位置："+s);
        alert.showAndWait();
    }

    public static void IpFormatError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("一些错误发生了！");
        alert.setHeaderText("IP地址的格式错误！");
        alert.setContentText("请确定IP地址的格式为：127.0.0.1:80");
        alert.showAndWait();
    }

    public static void IpValueError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("一些错误发生了！");
        alert.setHeaderText("IP地址的值错误！");
        alert.setContentText("请确定IP地址的四段均在0-255之间");
        alert.showAndWait();
    }

    public static void FileCreateError(String s,String str) {
        if(s==null||str==null) {
            NullPointerInputError(TAG+"FileCreateFail");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("一些错误发生了！");
        alert.setHeaderText("文件\""+s+"\"无法创建！");
        alert.setContentText("错误路径："+str);
        alert.showAndWait();
    }

    public static void OperateFail() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("操作失败！");
        alert.showAndWait();
    }

    public static void ThisFeatureIsNotOpen() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("该功能暂未开放！");
        alert.showAndWait();
    }

    public static void ArrayLengthError(String s, int i) {
        if(s==null) {
            NullPointerInputError(TAG+"ArrayLengthError");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("一些错误发生了！");
        alert.setHeaderText("数组长度不符合预期！");
        alert.setContentText("错误位置："+s+"\n长度为："+i);
        alert.showAndWait();
    }

    public static void UserExist() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("该用户已存在！");
        alert.showAndWait();
    }

    public static void UserNoExist() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("请输入正确的用户名与密码！");
        alert.showAndWait();
    }

    public static void InputLengthError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("用户名或密码的长度错误！");
        alert.showAndWait();
    }

    public static void FileExist(String s,String str) {
        if (s==null||str==null) {
            NullPointerInputError(TAG+"FileExist");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("文件(夹)"+s+"已存在！");
        alert.setContentText("错误路径："+str);
        alert.showAndWait();
    }

    public static void FileNoExist(String s,String str) {
        if (s==null||str==null) {
            NullPointerInputError(TAG+"FileNoExist");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("文件"+s+"不存在！");
        alert.setContentText("错误路径："+str);
        alert.showAndWait();
    }

    public static void InitFail() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("软件初始化失败！");
        alert.showAndWait();
    }

    public static void FileParserFail(String s,String str) {
        if (s==null||str==null) {
            NullPointerInputError(TAG+"FileParserFail");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("无法解析\""+s+"\"文件！");
        alert.setContentText("错误路径："+str);
        alert.showAndWait();
    }

    public static void FolderCreateFail(String s,String str) {
        if (s==null||str==null) {
            NullPointerInputError(TAG+"FileParserFail");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("无法创建\""+s+"\"文件夹！");
        alert.setContentText("错误路径："+str);
        alert.showAndWait();
    }

    public static void FileCreateFail(String s,String str) {
        if (s==null||str==null) {
            NullPointerInputError(TAG+"FileParserFail");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("无法创建\""+s+"\"文件！");
        alert.setContentText("错误路径："+str);
        alert.showAndWait();
    }

    public static void Error() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Error！");
        alert.showAndWait();
    }

    public static void NameError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("请输入正确的名称（至少有一个非空白字符且字符数小于255）！");
        alert.showAndWait();
    }

    public static void DateError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("请确保开始日期与结束日期正确！");
        alert.showAndWait();
    }

    public static void FileNameRepeat() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("文件名重复！");
        alert.showAndWait();
    }

    public static void FileDeleteFail(String s, String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("文件"+s+"无法删除！");
        alert.setContentText("错误路径："+str);
        alert.showAndWait();
    }

    public static void OperatorFinish() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("完成操作！");
        alert.showAndWait();
    }
}
