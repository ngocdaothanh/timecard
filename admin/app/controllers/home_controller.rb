class HomeController < ApplicationController
  def index
    
  end

  def logout
    session['is_authenticated'] = nil
    session['user_name'] = nil
    session['user_id'] = nil
    session['group_name'] = nil
    session['group_id'] = nil
    session['is_admin'] = nil
    go_home
  end

  def login
    if request.post?
      # authenticate
      @result = 'authenticated'

      session['is_authenticated'] = @result

      if(authenticate)
        session['is_authenticated'] = ''
        session['user_name'] = params[:employee][:user_name]
        session['user_id'] = ''
        session['group_name'] = ''
        session['group_id'] = ''
        session['is_admin'] = true
        go_home
      else
        @message = "Username and password are not matching!"
      end
    end
  end
end
