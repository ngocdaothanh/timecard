class HomeController < ApplicationController
  def index
    
  end

  def logout
    reset_session
    go_home
  end

  def login
    if request.post?
      # authenticate
      @users = User.find_all_by_username_and_encrypted_password(params[:user][:username], get_encrypt(params[:user][:password]))

      if @users.length == 0
        @message = "Username and password are not matching!"
      else
        @user = @users[0]
        session['is_authenticated'] = 'authenticated'
        session['user_name'] = @user.username
        session['user_id'] = @user.id
        session['group_id'] = @user.group_id
        session['is_admin'] = true
        go_home
      end
    end
  end
end
