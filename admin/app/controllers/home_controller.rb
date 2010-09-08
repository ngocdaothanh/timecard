class HomeController < ApplicationController
  def index
    @title = 'Home'
  end

  def logout
    @title = 'Home'
    reset_session
    go_home
  end

  def login
    @title = 'Login'
    if request.post?
      # authenticate
      @users = User.find_all_by_username_and_encrypted_password(params[:user][:username], get_encrypt(params[:user][:password]))

      if @users.length == 0
        @message = "Username and password are not matching!"
      else
        @user = @users[0]
        session['is_authenticated'] = true
        session['user_name'] = @user.username
        session['user_id'] = @user.id
        session['current_user'] = @user
        session['group_id'] = @user.group_id
        session['is_admin'] = is_admin(@user.username)
        session['is_authorized'] = false
        go_home
      end      
    end

  end

end