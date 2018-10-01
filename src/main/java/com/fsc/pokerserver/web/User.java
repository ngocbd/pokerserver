/*
The MIT License (MIT)
Copyright (c) 2018 by Ngocbd
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.fsc.pokerserver.web;

import com.fcs.pokerserver.*;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * The class to the User to login.
 *
 * @category com > fcs > pokerserver > web
 */
@Entity
public class User {
    @Id
    private String username;
    private String password;
    @Index
    private String token;

    private long balance;
    private String avatar_url;


    /**
     * Return the username
     *
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * The method to set username for the Player
     *
     * @param String username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Return the password
     *
     * @return String password
     */
    public String getPassword() {
        return password;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    /**
     * The method to set password for the Player
     *
     * @param String password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the token
     *
     * @return String token
     */
    public String getToken() {
        return token;
    }

    /**
     * The method to set token for the Player
     *
     * @param String token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Return the balance
     *
     * @return String balance
     */
    public long getBalance() {
        return balance;
    }

    /**
     * The method to set balance for the Player
     *
     * @param String balance
     */
    public void setBalance(long balance) {
        this.balance = balance;
    }


}
