'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 500));

      if (email && password) {
        // Store user data
        localStorage.setItem('userEmail', email);
        localStorage.setItem('userName', email.split('@')[0]);
        
        if (rememberMe) {
          localStorage.setItem('rememberMe', 'true');
        }

        // Redirect to dashboard
        router.push('/dashboard');
      }
    } catch (error) {
      console.error('Login error:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main className="flex justify-center items-center min-h-screen bg-gradient-to-br from-indigo-500 to-purple-600 px-5">
      <div className="bg-white rounded-lg shadow-2xl w-full max-w-md p-10 animate-in fade-in slide-in-from-top-5 duration-500">
        <div className="text-center mb-10">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">Welcome Back</h1>
          <p className="text-gray-500">Sign in to your account</p>
        </div>

        <form onSubmit={handleLogin}>
          <div className="mb-5">
            <label htmlFor="email" className="block mb-2 font-semibold text-gray-700 text-sm">
              Email Address
            </label>
            <input
              type="email"
              id="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-4 py-3 border border-gray-300 rounded-md text-sm transition-all focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
            />
          </div>

          <div className="mb-5">
            <label htmlFor="password" className="block mb-2 font-semibold text-gray-700 text-sm">
              Password
            </label>
            <input
              type="password"
              id="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-4 py-3 border border-gray-300 rounded-md text-sm transition-all focus:outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
            />
          </div>

          <div className="flex justify-between items-center mb-6 text-sm">
            <label className="flex items-center cursor-pointer text-gray-600">
              <input
                type="checkbox"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
                className="mr-2 w-4 h-4 cursor-pointer"
              />
              Remember me
            </label>
            <a href="#" className="text-indigo-500 hover:text-purple-600 transition-colors hover:underline">
              Forgot password?
            </a>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full py-3 bg-gradient-to-r from-indigo-500 to-purple-600 text-white rounded-md font-semibold transition-all hover:enabled:shadow-lg hover:enabled:-translate-y-0.5 active:enabled:translate-y-0 disabled:opacity-70 disabled:cursor-not-allowed"
          >
            {isLoading ? 'Signing In...' : 'Sign In'}
          </button>
        </form>

        <div className="relative my-6">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-gray-300"></div>
          </div>
          <div className="relative flex justify-center text-sm">
            <span className="px-2 bg-white text-gray-500">Or continue with</span>
          </div>
        </div>

        <button
          type="button"
          className="w-full py-3 border-2 border-gray-300 rounded-md font-semibold text-gray-700 transition-all hover:border-indigo-500 hover:bg-indigo-50 flex items-center justify-center gap-2"
        >
          <svg className="w-5 h-5" viewBox="0 0 24 24">
            <path
              fill="#4285F4"
              d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
            />
            <path
              fill="#34A853"
              d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
            />
            <path
              fill="#FBBC05"
              d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
            />
            <path
              fill="#EA4335"
              d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
            />
          </svg>
          Sign in with Google
        </button>

        <div className="text-center mt-6 text-sm text-gray-600">
          <p>
            Don't have an account? <a href="#" className="text-indigo-500 font-semibold hover:text-purple-600 transition-colors">Sign up here</a>
          </p>
        </div>
      </div>
    </main>
  );
}
