package com.demo.wave.test.unit.security;

import com.demo.wave.common.utility.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.util.SecurityConstants;

import java.io.FilePermission;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * {@link AccessController} serves the same purpose basically with {@link SecurityManager}
 * since the Java Security Model (JSM) is involving with the Java version updating
 *
 * @author Vince Yuan
 * @date 2021/12/7
 */
public class AccessControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(AccessControllerTest.class);

    private AccessControlContext accessControlContext;

    @Before
    public void testPreparation() {
        accessControlContext = AccessController.getContext();
        LOG.info("Access control context: {}", accessControlContext);

        System.out.println("testPreparation is completed");
    }

    @Test
    public void test1() {
        // Below is the default security policy that grants basic permission
        // for all Java users, which lies in "$JRE_HOME/lib/security/java.policy"
        /*
        // Standard extensions get all permissions by default
        grant codeBase "file:${{java.ext.dirs}}/*" {
                permission java.security.AllPermission;
        };
        // default permissions granted to all domains
        grant {
                // Allows any thread to stop itself using the java.lang.Thread.stop()
                // method that takes no argument.
                // Note that this permission is granted by default only to remain
                // backwards compatible.
                // It is strongly recommended that you either remove this permission
                // from this policy file or further restrict it to code sources
                // that you specify, because Thread.stop() is potentially unsafe.
                // See the API specification of java.lang.Thread.stop() for more
                // information.
                permission java.lang.RuntimePermission "stopThread";
                // allows anyone to listen on dynamic ports
                permission java.net.SocketPermission "localhost:0", "listen";
                // permission for standard RMI registry port
                permission java.net.SocketPermission "localhost:1099", "listen";
                // "standard" properies that can be read by anyone
                permission java.util.PropertyPermission "java.version", "read";
                permission java.util.PropertyPermission "java.vendor", "read";
                permission java.util.PropertyPermission "java.vendor.url", "read";
                permission java.util.PropertyPermission "java.class.version", "read";
                permission java.util.PropertyPermission "os.name", "read";
                permission java.util.PropertyPermission "os.version", "read";
                permission java.util.PropertyPermission "os.arch", "read";
                permission java.util.PropertyPermission "file.separator", "read";
                permission java.util.PropertyPermission "path.separator", "read";
                permission java.util.PropertyPermission "line.separator", "read";
                permission java.util.PropertyPermission "java.specification.version", "read";
                permission java.util.PropertyPermission "java.specification.vendor", "read";
                permission java.util.PropertyPermission "java.specification.name", "read";
                permission java.util.PropertyPermission "java.vm.specification.version", "read";
                permission java.util.PropertyPermission "java.vm.specification.vendor", "read";
                permission java.util.PropertyPermission "java.vm.specification.name", "read";
                permission java.util.PropertyPermission "java.vm.version", "read";
                permission java.util.PropertyPermission "java.vm.vendor", "read";
                permission java.util.PropertyPermission "java.vm.name", "read";
        };
         */
        // Custom policy file can be created (with the format as above)
        // if necessary and used through JVM argument:
        // -Djava.security.policy=<POLICY_FILE_URL>, which means to add a policy file
        // -Djava.security.policy==<POLICY_FILE_URL>, which means to use the policy file only

        System.out.println("testGrantPermission is completed");
    }

    @Test
    public void test2() {
        try {
            // Permissions to check
            Permission fileExecute = new FilePermission("D:/Software/Test", SecurityConstants.FILE_EXECUTE_ACTION);
            Permission fileRead = new FilePermission("D:/Software/Test", SecurityConstants.FILE_READ_ACTION);
            Permission fileReadLink = new FilePermission("D:/Software/Test", SecurityConstants.FILE_READLINK_ACTION);
            Permission fileWrite = new FilePermission("D:/Software/Test", SecurityConstants.FILE_WRITE_ACTION);
            Permission fileDelete = new FilePermission("D:/Software/Test", SecurityConstants.FILE_DELETE_ACTION);

            // Check permission
            AccessController.checkPermission(fileExecute);
            AccessController.checkPermission(fileRead);
            AccessController.checkPermission(fileReadLink);
            AccessController.checkPermission(fileWrite);
            AccessController.checkPermission(fileDelete);
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        }

        System.out.println("testCheckPermission is completed");
    }

    @Test
    public void test3() {
        // Perform an action with exception thrown with privilegeFile
        Path filePath = Paths.get("D:/Document/testDoPrivileged.txt");
        String fileContent = "Hello Do Privilege with Exception Thrown";
        try {
            boolean fileCreatedSuccessfully = AccessController.doPrivileged((PrivilegedExceptionAction<Boolean>) () -> {
                Files.deleteIfExists(filePath);
                Path filePathCreated = Files.createFile(filePath);
                return filePathCreated != null;
            });
            LOG.info("File {} created successfully: {}", filePath, fileCreatedSuccessfully);
            int numberOfBytesWritten = AccessController.doPrivileged((PrivilegedExceptionAction<Integer>) () -> {
                FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.WRITE);
                return fileChannel.write(ByteBuffer.wrap(fileContent.getBytes()));
            });
            LOG.info("Number of bytes written to {}: {}", filePath, numberOfBytesWritten);
            String fileContentRead = AccessController.doPrivileged((PrivilegedExceptionAction<String>)() -> {
                FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ);
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int numberOfBytesRead = fileChannel.read(byteBuffer);
                return numberOfBytesRead > 0 ? new String(byteBuffer.array()) : StringUtils.BLANK_STRING;
            });
            LOG.info("Content read from {}: {}", filePath, fileContentRead);
            boolean fileDeletedSuccessfully = AccessController.doPrivileged((PrivilegedExceptionAction<Boolean>) () -> {
                Files.delete(filePath);
                return true;
            });
            LOG.info("File {} deleted successfully: {}", filePath, fileDeletedSuccessfully);
        } catch (PrivilegedActionException e) {
            LOG.error("Exception of privileged action caught", e);
        }

        // Perform an action without exception thrown with privilege
        String configuredValue = AccessController.doPrivileged((PrivilegedAction<String>) () -> {
            String propertyValue = System.getProperty("java.test.security", "HelloSecurity");
            return propertyValue;
        });
        LOG.info("Configured value: {}", configuredValue);

        System.out.println("testDoPrivileged is completed");
    }

    @Test
    public void test4() {
        try {
            // Permissions to check
            Permission fileExecute = new FilePermission("D:/Software/Test", SecurityConstants.FILE_EXECUTE_ACTION);
            Permission fileRead = new FilePermission("D:/Software/Test", SecurityConstants.FILE_READ_ACTION);
            Permission fileReadLink = new FilePermission("D:/Software/Test", SecurityConstants.FILE_READLINK_ACTION);
            Permission fileWrite = new FilePermission("D:/Software/Test", SecurityConstants.FILE_WRITE_ACTION);
            Permission fileDelete = new FilePermission("D:/Software/Test", SecurityConstants.FILE_DELETE_ACTION);

            // Check permission
            accessControlContext.checkPermission(fileExecute);
            accessControlContext.checkPermission(fileRead);
            accessControlContext.checkPermission(fileReadLink);
            accessControlContext.checkPermission(fileWrite);
            accessControlContext.checkPermission(fileDelete);
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        }

        System.out.println("testAccessControlContext is completed");
    }

    @After
    public void testCompletion() {
        System.out.println("testCompletion is completed");
    }
}
