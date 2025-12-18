package org.ph.share.juc.lock;

import org.ph.share.SmallTool;

import java.util.concurrent.Semaphore;

public class LoginService {

    private final Semaphore semaphore;

    public LoginService(int maxPermitLoginAccount) {
        this.semaphore = new Semaphore(maxPermitLoginAccount, true);
    }

    public boolean login() {
        // 尝试获取令牌
        boolean login = semaphore.tryAcquire();
        if (login) {
            SmallTool.printTimeAndThread(Thread.currentThread().getName() + "==  登录成功");
        }
        return login;
    }

    public void logout() {
        //  释放令牌许可
        semaphore.release();
        SmallTool.printTimeAndThread(Thread.currentThread().getName() + "==  登出成功");
    }

}
