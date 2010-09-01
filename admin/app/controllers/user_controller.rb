class UserController < ApplicationController
  def create
    # only administrator allows create an user account
    if not session['is_admin']
      go_home
    end
    if request.post? and
      if params[:user][:password] == params[:user][:cfm_password]
#        User.create(:username => params[:user][:username],
#                    :name => params[:user][:name],
#                    :password => params[:user][:password],
#                    :group_id => params[:user][:group_id],
#                    :manager_id => params[:user][:manager_id])
      else
        @message = 'password and confirm password are not matching!'
      end
    end
  end
end
