import java.util.Scanner;

//1为有权限，0为无权限
public class File {
    FOLDER root;//根目录
    int count=0;//控制输出格式
    int flagD=0;//删除标记
    public static void main(String[] args) {
        File init=new File();
        init.initRootFolder();
        init.menu();
    }

    FOLDER findCurrentFolder(FOLDER currentFolder,String name) {//查找指定目录
        FOLDER folder;
        if(currentFolder==null)
            return null;		//没找到
        if(currentFolder.name.equals(name))
            //System.out.println("当前目录："+currentFolder.name+" 指定目录："+name);
            return currentFolder;		//查找目录为当前目录
        folder=findCurrentFolder(currentFolder.firstChildFolder,name);
        if(folder!=null)
            return folder;		//	查找目录在子目录中
        folder=findCurrentFolder(currentFolder.nextFolder,name);
        if(folder!=null)
            return folder;		//查找目录在同级其他目录中
        return null;		//没找到
    }

    FILE findCurrentFile(FOLDER currentFolder,String name) {//查找指定文件
        FILE tempFile;
        if(currentFolder==null)
            return null;		//没找到
        tempFile=currentFolder.firstChildFile;
        while(tempFile!=null) {//	遍历当前目录子文件
            System.out.println(tempFile.name+" "+name);
            if(tempFile.name.equals(name))
                return tempFile;		//找到了
            tempFile=tempFile.nextFile;
        }
        tempFile=findCurrentFile(currentFolder.firstChildFolder,name);
        if(tempFile!=null)
            return tempFile;		//查找文件在子目录中
        tempFile=findCurrentFile(currentFolder.nextFolder,name);
        if(tempFile!=null)
            return tempFile;		//查找文件在同级其他目录中

        return null;		//没找到
    }

    FOLDER prepareWorkBeforeCreate(){
        Scanner in=new Scanner(System.in);
        FOLDER currentFolder=null;
        String name=new String();
        System.out.println("输入当前目录名称：");
        name=in.nextLine();

        currentFolder=findCurrentFolder(root,name);
        if(currentFolder==null) {
            System.out.println("目录不存在！");

            return null;
        }
        if(currentFolder.canWrite==0) {
            System.out.println("权限不够，无法创建！");

            return null;
        }

        return currentFolder;
    }

    void createFolder() {		//当前目录中创建新目录
        Scanner in=new Scanner(System.in);
        FOLDER currentFolder=prepareWorkBeforeCreate();
        if(currentFolder==null) {		//目标目录不存在
            return;
        }
        String name;
        System.out.println("输入新目录名称：");
        name=in.nextLine();

        FOLDER newFolder;
        newFolder=new FOLDER();
        newFolder.name=name;
        newFolder.firstChildFolder=null;		//初始化新目录
        newFolder.firstChildFile=null;
        newFolder.nextFolder=null;
        newFolder.parentFolder=null;
        newFolder.frontFolder=null;
        newFolder.canRead=1;
        newFolder.canWrite=1;

        if(currentFolder.firstChildFolder==null) {		//当前目录下无子目录
            currentFolder.firstChildFolder=newFolder;//设置当前目录的子目录为新目录
            newFolder.parentFolder=currentFolder;//新目录的父目录为当前目录
        }
        else {		//当前目录下有子目录
            FOLDER tempFolder=currentFolder.firstChildFolder;//保存当前目录的子目录
            FOLDER lastFolder=new FOLDER();		//保存当前currentFolder下最后一个子folder
            while(tempFolder!=null) {		//	同级目录下不得有相同的目录
                lastFolder=tempFolder;
                if(tempFolder.name.equals(newFolder.name)) {
                    System.out.println("目录下已有同名目录: "+currentFolder.name);
                    return;
                }
                tempFolder=tempFolder.nextFolder;//往下走，遍历当前目录下的所有子目录

            }
            lastFolder.nextFolder=newFolder;		//将新目录与同级子目录建立连接
            newFolder.frontFolder=lastFolder;
        }
        System.out.println("创建成功！");
        System.out.println("当前所在目录名为："+currentFolder.name);
        System.out.println("创建的目录名为： "+newFolder.name);

    }

    void createFile() {		//当前目录中创建新文件
        Scanner in=new Scanner(System.in);
        FOLDER currentFolder=prepareWorkBeforeCreate();
        if(currentFolder==null) {//当前目标不存在
            return;
        }
        String name;
        System.out.println("请输入新文件名称：");
        name=in.nextLine();

        FILE newFile;
        newFile=new FILE();
        newFile.name=name;
        System.out.println("是否输入文件内容？是，请输入y：");
        String ans=in.nextLine();
        System.out.println("ans:"+ans);
        if(ans.equals("y")) {
            System.out.println("请输入文件内容：");
            newFile.content=in.nextLine();

        }
        else {
            newFile.content="";
        }


        newFile.nextFile=null;		//初始化新文件信息
        newFile.frontFile=null;
        newFile.parentFolder=null;
        newFile.canRead=1;
        newFile.canWrite=1;

        if(currentFolder.firstChildFile==null) {		//当前目录下无子文件
            currentFolder.firstChildFile=newFile;
            newFile.parentFolder=currentFolder;
        }
        else {		//当前目录下有子文件
            FILE tempFile=currentFolder.firstChildFile;
            FILE lastFile=new FILE();		//保存当前currentFolder下最后一个子文件

            while(tempFile!=null) {		//同级目录下不得有相同文件
                lastFile=tempFile;
                if(tempFile.name.equals(newFile.name)) {
                    System.out.println("目录下已有同名文件："+currentFolder.name);
                    return;
                }
                tempFile=tempFile.nextFile;
            }
            lastFile.nextFile=newFile;		//将新文件与同级文件建立连接
            newFile.frontFile=lastFile;

        }
        System.out.println("创建成功！");
    }

    void deleteAllChild(FOLDER currentFolder) {		//删除该目录下所有内容
        FILE tempFile,dFile;
        if(currentFolder==null)
            return;
        if(flagD==0)
            deleteAllChild(currentFolder.nextFolder);
        flagD=1;
        deleteAllChild(currentFolder.firstChildFolder);//遍历子目录
        tempFile=currentFolder.firstChildFile;
        while(tempFile!=null) {//删除该目录子文件
            dFile=tempFile;
            tempFile=tempFile.nextFile;
        }
    }

    void deleteFoder() {//删除目录所有内容
        Scanner in=new Scanner(System.in);
        String name;
        System.out.println("请输入要删除的目录名称：");
        name=in.nextLine();
        if(name.equals("root")) {
            System.out.println("根目录不能删除！");
            return;
        }
        FOLDER currentFolder=findCurrentFolder(root, name);//先在根目录中找到当前目录
        if(currentFolder==null) {
            System.out.println("目录不存在！");
            return;
        }
        if(currentFolder.canWrite==0) {
            System.out.println("权限不够，无法删除！");
            return ;
        }
        if(currentFolder.frontFolder==null) {//如果当前目录是父级目录的第一个目录
            currentFolder.parentFolder.firstChildFolder=currentFolder.nextFolder;//断开连接
            if(currentFolder.nextFolder!=null)
                currentFolder.nextFolder.frontFolder=null;//设置当前目录的下一个目录为父级目录的第一个目录
        }
        else
            currentFolder.frontFolder.nextFolder=currentFolder.nextFolder;//连接当前目录的上一个目录和下一个目录
        deleteAllChild(currentFolder);//释放空间
        System.out.println("删除成功！");
    }

    void deleteFile() {//删除文件
        String name;
        Scanner in=new Scanner(System.in);
        System.out.println("请输入要删除的文件名称：");
        name=in.nextLine();
        FILE currentFile=findCurrentFile(root, name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return;
        }
        if(currentFile.frontFile==null) {
            currentFile.parentFolder.firstChildFile=currentFile.nextFile;//如果当前文件的前面没有文件，那么设置当前文件的父目录下的第一个文件为当前文件的下一个文件
            if(currentFile.nextFile!=null)
                currentFile.nextFile.frontFile=null;//当前文件的下一个文件没有上一个文件，因为当前文件要被删除了
        }
        else
            currentFile.frontFile.nextFile=currentFile.nextFile;//将当前文件的上一个文件和下一个文件连接起来
        System.out.println("删除成功！");
    }

    void displayFileSystemStructure(FOLDER currentFolder) {//输出目录结构
        FILE tempFile;
        if(currentFolder!=null&&currentFolder.canRead==1) {//如果为1，那么有权限
            for(int i=0;i<count;i++)
                System.out.print("");
            System.out.println("|-");
            System.out.println("("+currentFolder.name+")");
            if(currentFolder.nextFolder!=null) {
                System.out.println(currentFolder.name+"目录的同级下一目录为："+currentFolder.nextFolder.name);
            }
            int length=15-count*2-currentFolder.name.length();
            for(int i=0;i<length;i++)
                System.out.print("");
            System.out.println(currentFolder.canRead+" "+currentFolder.canWrite);

        }
        else if(currentFolder==null) {


            count--;
            return;
        }
        count++;
        tempFile=currentFolder.firstChildFile;
        while(tempFile!=null) {
            if(tempFile.canRead==1) {
                for(int i=0;i<count;i++)
                    System.out.print("");
                System.out.println("|-");
                System.out.println(tempFile.name);
                int length=20-count*2-tempFile.name.length();
                for(int i=0;i<length;i++)
                    System.out.print("");
                System.out.println(currentFolder.canRead+" "+currentFolder.canWrite);
            }
            tempFile=tempFile.nextFile;
        }
        displayFileSystemStructure(currentFolder.firstChildFolder);//遍历子目录
        displayFileSystemStructure(currentFolder.nextFolder);//遍历同级目录
    }

    void showFileContent() {//显示文件内容
        Scanner in=new Scanner(System.in);
        FILE currentFile=null;
        String name;
        System.out.println("输入文件名：");
        name=in.nextLine();

        currentFile=findCurrentFile(root, name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return;
        }
        if(currentFile.canRead==0) {
            System.out.println("权限不够，无法读取！");
            return;
        }
        System.out.println("文件内容："+currentFile.content);
        System.out.println("文件长度："+currentFile.content.length());
    }

    void changeFileContent() {//更改文件内容
        Scanner inScanner=new Scanner(System.in);
        FILE currentFile=null;
        String name;
        System.out.println("请输入文件名：");
        name=inScanner.nextLine();
        inScanner.nextLine();
        currentFile=findCurrentFile(root, name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return;
        }
        if(currentFile.canWrite==0) {
            System.out.println("权限不够，不能修改！");
            return;
        }
        System.out.println("请输入文件内容：");
        currentFile.content=inScanner.nextLine();
        inScanner.nextLine();
        System.out.println("更改成功！");

    }

    void changeAllChildPermission(FOLDER currentFolder,int canRead,int canWrite) {//更改子目录下所有目录和文件权限
        FILE tempFile;
        if(currentFolder!=null) {
            currentFolder.canRead=canRead;
            currentFolder.canWrite=canWrite;
        }
        else return;
        tempFile=currentFolder.firstChildFile;
        while(tempFile!=null) {//遍历子文件
            tempFile.canRead=canRead;
            tempFile.canWrite=canWrite;
            tempFile=tempFile.nextFile;
        }
        changeAllChildPermission(currentFolder.firstChildFolder, canRead, canWrite);
        if(currentFolder.firstChildFolder!=null)
            changeAllChildPermission(currentFolder.firstChildFolder.nextFolder, canRead, canWrite);
    }

    void changeFolderPermission() {//更改目录权限
        Scanner in=new Scanner(System.in);
        String name;

        System.out.println("请输入要更改权限的目录名称：");
        name=in.nextLine();
        if(name.equals("root")) {
            System.out.println("根目录不准修改");

            return;
        }
        FOLDER currentFolder=null;
        currentFolder=findCurrentFolder(root, name);
        if(currentFolder==null) {
            System.out.println("目录不存在");
            return;
        }
        System.out.println("输入目录权限（读和写）：");
        String tempInput=in.nextLine();

        currentFolder.canRead=tempInput.charAt(0);
        currentFolder.canWrite=tempInput.charAt(1);
        changeAllChildPermission(currentFolder, currentFolder.canRead, currentFolder.canWrite);//更改当前目录权限时，同时更改目录下所有文件和目录权限
        System.out.println("权限更改成功！");
    }

    void changeFilePermission() {//更改文件权限
        Scanner in=new Scanner(System.in);
        String name;
        System.out.println("请输入要更改权限的的文件名称：");
        name=in.nextLine();
        FILE currentFile=null;
        currentFile=findCurrentFile(root, name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return;
        }
        System.out.println("输入文件权限（读和写）：");
        String tempInput=in.nextLine();

        currentFile.canRead=tempInput.charAt(0);
        currentFile.canWrite=tempInput.charAt(1);
        System.out.println("权限更改成功！");

    }

    int fileNameIsDuplication(FILE currentFile,String name) {//判断新重命名的文件是否重名
        FILE tempFile=currentFile.frontFile;
        while(tempFile!=null) {
            if(tempFile.name.equals(name)) {
                System.out.println("文件重名！");
                return 1;
            }
            tempFile=tempFile.nextFile;
        }
        tempFile=currentFile.nextFile;
        while(tempFile!=null) {
            if(tempFile.name.equals(name)) {
                System.out.println("文件重名！");
                return 1;
            }
            tempFile=tempFile.nextFile;
        }
        return 0;
    }

    int folderNameIsDuplication(FOLDER currentFolder,String name) {//判断新重命名的目录是否重名
        FOLDER tempFolder=currentFolder.frontFolder;
        while(tempFolder!=null) {
            if(tempFolder.name.equals(name)) {
                System.out.println("目录重名！");
                return 1;
            }
            tempFolder=tempFolder.frontFolder;
        }
        tempFolder=currentFolder.nextFolder;
        while(tempFolder!=null) {
            if(tempFolder.name.equals(name)) {
                System.out.println("目录重名！");
                return 1;
            }
            tempFolder=tempFolder.nextFolder;
        }
        return 0;
    }

    void renameFolder() {//重命名目录
        Scanner in=new Scanner(System.in);
        String name;
        System.out.println("请输入要重命名的目录名称：");
        name=in.nextLine();
        if(name.equals("root")) {
            System.out.println("根目录不准修改");

            return;
        }
        FOLDER currentFolder=findCurrentFolder(root, name);
        if(currentFolder==null) {
            System.out.println("目录不存在！");

            return;
        }
        System.out.println("输入新目录名称：");
        name=in.nextLine();


        if(folderNameIsDuplication(currentFolder, name)==1)//重名
            return;
        currentFolder.name=name;
        System.out.println("重命名成功！");
    }

    void renameFile() {//重命名文件
        Scanner in=new Scanner(System.in);
        String name;
        System.out.println("请输入要重命名的文件名称：");
        name=in.nextLine();
        FILE currentFile=findCurrentFile(root, name);
        if(currentFile==null) {
            System.out.println("文件不存在！");
            return;
        }
        System.out.println("输入新名称：");
        name=in.nextLine();

        if(fileNameIsDuplication(currentFile, name)==1)//重名是1，为0则不重名
            return;
        currentFile.name=name;
        System.out.println("重命名成功！");

    }

    void menu() {//菜单
        Scanner in = new Scanner(System.in);
        int choice;
        System.out.println("-----------------------");
        System.out.println("1.新建目录");
        System.out.println("2.删除目录");
        System.out.println("3.重命名目录");
        System.out.println("4.更改目录权限");
        System.out.println("5.创建文件");
        System.out.println("6.删除文件");
        System.out.println("7.重命名文件");
        System.out.println("8.显示文件内容");
        System.out.println("9.更改文件内容");
        System.out.println("10.更改文件权限");
        System.out.println("0.退出");
        System.out.println("-----------------------");
        count=0;
        System.out.println("根目录为："+root.name);
        System.out.println("目录结构：");

        displayFileSystemStructure(root);
        while(true) {
            System.out.println("请选择操作命令：");
            choice=in.nextInt();

            switch(choice) {
                case 1:
                    createFolder();
                    break;
                case 2:deleteFoder();
                    break;
                case 3:renameFolder();
                    break;
                case 4:changeFolderPermission();
                    break;
                case 5:createFile();
                    break;
                case 6:deleteFile();
                    break;
                case 7:renameFile();
                    break;
                case 8:showFileContent();
                    break;
                case 9:changeFileContent();
                    break;
                case 10:changeFilePermission();
                    break;
                case 0:return;
                default: break;
            }
            count=0;
            System.out.println("目录结构：");
            displayFileSystemStructure(root);
        }

    }

    void initRootFolder() {//初始化根目录信息
        root=new FOLDER();
        root.frontFolder=null;
        root.nextFolder=null;
        root.parentFolder=null;
        root.firstChildFile=null;
        root.firstChildFolder=null;
        root.canRead=1;
        root.canWrite=1;
        root.name="root";
    }

}
class FOLDER{//目录结构
    String name= "";//目录名
    int canRead;//是否可读
    int canWrite;//是否可写
    FOLDER nextFolder;//同级下一目录
    FOLDER frontFolder;//同级上一目录
    FOLDER parentFolder;//父目录
    FOLDER firstChildFolder;//子目录
    FILE firstChildFile;//子文件
}
class FILE{
    String name= "";//文件名
    String content= "";//文件内容
    int canRead;
    int canWrite;
    FILE frontFile;//同级目录上一文件
    FILE nextFile;//同级目录下一文件
    FOLDER parentFolder;//父目录
}
