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
    if not is_admin_
      go_home
    end

    @groups = Group.all
    @users = User.all

    if request.post?
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
    if authenticate     # authentication
      if request.post?
        if is_admin_      # administrator authorization
          @user = User.find(params[:user][:id])
          if params[:user][:password] == params[:user][:cfm_password]
            if params[:user][:password] == ''
              @user.update_attributes(
                  :username  => params[:user][:username],
                  :name      => params[:user][:name],
                  :group_id  => params[:user][:group_id],
                  :manager_id  => params[:user][:manager_id],
                  :disabled    => params[:user][:disabled])
            else
              puts "Truoc khi cap nhat : encrypt #{@user.encrypted_password}"
              @user.update_attributes(
                  :username  => params[:user][:username],
                  :password  => params[:user][:password],
                  :name      => params[:user][:name],
                  :group_id  => params[:user][:group_id],
                  :manager_id  => params[:user][:manager_id],
                  :disabled    => params[:user][:disabled])
               puts "Sau khi cap nhat : encrypt #{@user.encrypted_password}"
            end
            redirect_to user_index_path
          else
            @groups = Group.all
            @users = User.all
            @message = 'Password and confirm password are not matching'
          end
        else
          if session['user_id'] == params[:user][:id]   # edit only your record
            @user = User.find(params[:user][:id])
            if params[:user][:password] == params[:user][:cfm_password]
              if params[:user][:password] != ''
                @user.update_attributes(:password => params[:user][:password])
              end
              redirect_to user_index_path
            else
              @message = 'Password and confirm password are not matching'
            end
          end
        end
      else
        if is_admin_      # administrator authorization
          @groups = Group.all
          @users = User.all
          @user = User.find(params[:id])
        else              # regular user authorization
          if session['user_id'] == params[:id] # ok, you can edit your record
            session['is_authorized'] = true
            @user = User.find(params[:id])
          else                                  # no, you can not edit others
            session['is_authorized'] = false
            @message = 'You can only edit your record'
          end
        end
      end
    else
      must_login
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