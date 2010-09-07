class UserController < ApplicationController
  def index
    if authenticate
      @users = User.all
    else
      must_login
    end
  end

  def create
    # only administrator allows create an user account
    if not session['is_admin']
      go_home
    end

    @groups = Group.all
    @users = User.all

    if request.post? and
      if params[:user][:password] == params[:user][:cfm_password]
        User.create(:username     => params[:user][:username],
                    :name         => params[:user][:name],
                    :password     => params[:user][:password],
                    :group_id     => params[:user][:group_id] == ""? nil : params[:user][:group_id],
                    :manager_id   => params[:user][:manager_id] == ""? nil : params[:user][:manager_id])
      else
        @message = 'password and confirm password are not matching!'
      end
    end
  end

  def edit
    if is_admin_
      @user = User.find(params[:id])
      if @user.nil?
      else

      end
      @message = ''
    else
      @message = 'Only administrator has deleted permisson'
    end
  end

  def delete
    if is_admin_
      @user = User.find(params[:id])
      if @user.nil?
      else
        @user.destroy
      end
      @message = ''
    else
      @message = 'Only administrator has deleted permisson'
    end
    redirect_to :controller => 'user', :action => 'index'
  end
end