/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ethercamp.vmtrace.sevice.console.crash.plugin.js;

import org.crsh.cli.impl.Delimiter;
import org.crsh.cli.impl.completion.CompletionMatch;
import org.crsh.cli.spi.Completion;
import org.crsh.command.CommandContext;
import org.crsh.lang.spi.Language;
import org.crsh.lang.spi.Repl;
import org.crsh.lang.spi.ReplResponse;
import org.crsh.shell.ErrorKind;
import org.crsh.shell.impl.command.RuntimeContextImpl;
import org.crsh.shell.impl.command.ShellSession;
import org.crsh.shell.impl.command.spi.Command;
import org.crsh.shell.impl.command.spi.CommandException;
import org.crsh.shell.impl.command.spi.CommandInvoker;
import org.crsh.util.Utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author Julien Viet */
public class JSRepl implements Repl {

  /** . */
  private static final JSRepl instance = new JSRepl();

  /** . */
  static final Logger log = Logger.getLogger(JSRepl.class.getName());

  public static JSRepl getInstance() {
    return instance;
  }

  /** . */
  private final Language lang = new Language() {
    @Override
    public String getName() { return "js"; }
    @Override
    public String getDisplayName() { return "JS 1.0"; }
    @Override
    public boolean isActive() { return true; }
    @Override
    public Repl getRepl() { return JSRepl.this; }
    @Override
    public org.crsh.lang.spi.Compiler getCompiler() { return ScriptCompiler.instance; }
    @Override
    public void init(ShellSession session) { }
    @Override
    public void destroy(ShellSession session) { }
  };

  ScriptEngine engine;
  private JSRepl() {
    ScriptEngineManager factory = new ScriptEngineManager();
    // create a Nashorn script engine
    engine = factory.getEngineByName("nashorn");
    try {
//      engine.eval(new FileReader("src/main/js/bignumber.min.js"));
//      engine.eval(new FileReader("src/main/js/env.nashorn.1.2-debug.js"));
//      engine.eval(new FileReader("src/main/js/web3-light.js"));
      engine.eval(new InputStreamReader(ClassLoader.getSystemResourceAsStream("js/bignumber.min.js")));
      engine.eval(new InputStreamReader(ClassLoader.getSystemResourceAsStream("js/env.nashorn.1.2-debug.js")));
      engine.eval(new InputStreamReader(ClassLoader.getSystemResourceAsStream("js/web3-light.js")));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Language getLanguage() {
    return lang;
  }

  public String getName() {
    return "js";
  }

  @Override
  public String getDescription() {
    return "JS test";
  }

  public ReplResponse eval(ShellSession session, String request) {
      CommandInvoker<Void, Object> invoker = new CommandInvoker<Void, Object>() {
        public void provide(Void element) throws IOException {
          throw new UnsupportedOperationException("Should not be invoked");
        }

        public Class<Void> getConsumedType() {
          return Void.class;
        }

        public void flush() throws IOException {
        }

        public Class<Object> getProducedType() {
          return Object.class;
        }

        CommandContext<Object> consumer;
        public void open(CommandContext<? super Object> consumer) throws IOException, CommandException {
        this.consumer = (CommandContext<Object>)consumer;
          try {
            Object o = engine.eval(request);
            if (o != null) {
              consumer.provide(o);
            }
          } catch (IOException e) {
            throw e;
          } catch (CommandException e) {
            throw e;
          } catch (Exception e) {
            throw new CommandException(ErrorKind.EVALUATION, "An error occured during the evalution of '" + request + "'", e);
          }
        }

        public void close() throws IOException, CommandException {
          try {
            consumer.flush();
            consumer.close();
          }
          catch (Exception e) {
            throw new CommandException(ErrorKind.EVALUATION, "An error occured during the evalution of '" + request + "'", e);
          }
        }
      };

      return new ReplResponse.Invoke(invoker);
//      Object eval = engine.eval(request);
//      System.out.println("====: " + eval);
//      return new ReplResponse.Response(ShellResponse.ok());
//    } catch (ScriptException e) {
//      return new ReplResponse.Response(ShellResponse.error(ErrorKind.EVALUATION, e.getMessage(), e.getCause()));
//    }
//    PipeLineFactory factory;
//    try {
//      factory = Token.parse(request).createFactory();
//    }
//    catch (CommandException e) {
//      return new ReplResponse.Response(ShellResponse.error(e.getErrorKind(), e.getMessage(), e.getCause()));
//    }
//    if (factory != null) {
//      try {
//        CommandInvoker<Void, Object> invoker = factory.create(session);
//        return new ReplResponse.Invoke(invoker);
//      }
//      catch (CommandNotFoundException e) {
//        log.log(Level.FINER, "Could not create command", e);
//        return new ReplResponse.Response(ShellResponse.unknownCommand(e.getName()));
//      }
//      catch (CommandException e) {
//        log.log(Level.FINER, "Could not create command", e);
//        return new ReplResponse.Response(ShellResponse.error(e.getErrorKind(), e.getMessage(), e));
//      }
//    } else {
//      return new ReplResponse.Response(ShellResponse.noCommand());
//    }
  }

  public CompletionMatch complete(ShellSession session, String prefix) {
    Token ast = Token.parse(prefix);
    String termPrefix;
    if (ast != null) {
      Token last = ast.getLast();
      termPrefix = Utils.trimLeft(last.value);
    } else {
      termPrefix = "";
    }

    //
    log.log(Level.FINE, "Retained term prefix is " + termPrefix);
    CompletionMatch completion;
    int pos = termPrefix.indexOf(' ');
    if (pos == -1) {
      Completion.Builder builder = Completion.builder(termPrefix);
      for (Map.Entry<String, String> command : session.getCommands()) {
        String name = command.getKey();
        if (name.startsWith(termPrefix)) {
          builder.add(name.substring(termPrefix.length()), true);
        }
      }
      completion = new CompletionMatch(Delimiter.EMPTY, builder.build());
    } else {
      String commandName = termPrefix.substring(0, pos);
      termPrefix = termPrefix.substring(pos);
      try {
        Command<?> command = session.getCommand(commandName);
        if (command != null) {
          completion = command.complete(new RuntimeContextImpl(session, session.getContext().getAttributes()), termPrefix);
        } else {
          completion = new CompletionMatch(Delimiter.EMPTY, Completion.create());
        }
      }
      catch (CommandException e) {
        log.log(Level.FINE, "Could not create command for completion of " + prefix, e);
        completion = new CompletionMatch(Delimiter.EMPTY, Completion.create());
      }
    }

    //
    return completion;
  }
}
